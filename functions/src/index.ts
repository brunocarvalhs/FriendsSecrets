import { onCall, HttpsError } from "firebase-functions/v2/https";
import { defineSecret } from "firebase-functions/params";
import { logger } from "firebase-functions";
import Anthropic from "@anthropic-ai/sdk";
import { zodOutputFormat } from "@anthropic-ai/sdk/helpers/zod";
import { z } from "zod";

const anthropicApiKey = defineSecret("ANTHROPIC_API_KEY");

const MAX_INTERESTS = 10;
const MAX_INTEREST_LENGTH = 100;
const MAX_GIFT_TYPE_LENGTH = 60;

const GiftSuggestionsSchema = z.object({
  suggestions: z
    .array(
      z.object({
        title: z.string(),
        reason: z.string(),
      })
    )
    .length(3),
});

interface SuggestGiftsRequest {
  interests?: unknown;
  minPrice?: unknown;
  maxPrice?: unknown;
  giftType?: unknown;
}

/**
 * Suggests three gift ideas for a Secret Santa recipient based on their
 * wishlist ("likes") and, optionally, the group's price range and gift
 * type. Called by SuggestGiftsUseCase on the Android client via the
 * Firebase Functions client SDK (a callable function, not a bare HTTPS
 * endpoint) so CORS and payload parsing are handled automatically.
 *
 * The Anthropic API key never reaches the client: it's stored as a Cloud
 * Functions secret and only ever read here, server-side.
 */
export const suggestGifts = onCall(
  { secrets: [anthropicApiKey], cors: true },
  async (request) => {
    const data = request.data as SuggestGiftsRequest;
    const interests = sanitizeInterests(data.interests);

    if (interests.length === 0) {
      throw new HttpsError(
        "invalid-argument",
        "At least one wishlist item (interest) is required."
      );
    }

    const minPrice = typeof data.minPrice === "number" ? data.minPrice : undefined;
    const maxPrice = typeof data.maxPrice === "number" ? data.maxPrice : undefined;
    const giftType =
      typeof data.giftType === "string"
        ? data.giftType.slice(0, MAX_GIFT_TYPE_LENGTH)
        : undefined;

    const client = new Anthropic({ apiKey: anthropicApiKey.value() });

    try {
      const response = await client.messages.parse({
        model: "claude-opus-5",
        max_tokens: 2048,
        system:
          "Você é um assistente de um app de Amigo Secreto que sugere ideias de " +
          "presentes criativas, específicas e viáveis de comprar no Brasil. " +
          "Responda sempre em português do Brasil.",
        messages: [
          {
            role: "user",
            content: buildPrompt({ interests, minPrice, maxPrice, giftType }),
          },
        ],
        output_config: {
          format: zodOutputFormat(GiftSuggestionsSchema),
        },
      });

      if (!response.parsed_output) {
        throw new HttpsError(
          "internal",
          "Could not parse gift suggestions from the model response."
        );
      }

      return response.parsed_output;
    } catch (error) {
      if (error instanceof HttpsError) {
        throw error;
      }
      logger.error("suggestGifts failed", error);
      throw new HttpsError("internal", "Failed to generate gift suggestions.");
    }
  }
);

function sanitizeInterests(value: unknown): string[] {
  if (!Array.isArray(value)) {
    return [];
  }

  return value
    .filter((item): item is string => typeof item === "string")
    .map((item) => item.trim().slice(0, MAX_INTEREST_LENGTH))
    .filter((item) => item.length > 0)
    .slice(0, MAX_INTERESTS);
}

function buildPrompt(params: {
  interests: string[];
  minPrice?: number;
  maxPrice?: number;
  giftType?: string;
}): string {
  const lines = [
    "Sugira exatamente 3 ideias de presente para uma pessoa com os seguintes interesses/itens de lista de desejos:",
    params.interests.map((interest) => `- ${interest}`).join("\n"),
  ];

  if (params.minPrice !== undefined || params.maxPrice !== undefined) {
    const range =
      params.minPrice !== undefined && params.maxPrice !== undefined
        ? `entre R$ ${params.minPrice} e R$ ${params.maxPrice}`
        : params.minPrice !== undefined
          ? `a partir de R$ ${params.minPrice}`
          : `até R$ ${params.maxPrice}`;
    lines.push(`Orçamento: ${range}.`);
  }

  if (params.giftType) {
    lines.push(`Tipo de presente combinado no grupo: ${params.giftType}.`);
  }

  lines.push(
    "Para cada sugestão, dê um título curto (poucas palavras) e uma razão breve (1 frase) " +
      "conectando a sugestão a um dos interesses listados."
  );

  return lines.join("\n");
}
