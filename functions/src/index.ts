import { initializeApp } from "firebase-admin/app";
import { getFirestore } from "firebase-admin/firestore";
import { getMessaging } from "firebase-admin/messaging";
import { logger } from "firebase-functions";
import { onDocumentUpdated } from "firebase-functions/v2/firestore";
import { onValueCreated } from "firebase-functions/v2/database";

initializeApp();

const firestore = getFirestore();
const messaging = getMessaging();

/**
 * Notifies every registered device in a group when the Secret Santa draw
 * transitions from "not drawn" to "drawn". Tokens live in the flat
 * top-level `push_tokens` collection (document id `${groupId}_${deviceId}`,
 * with `groupId`/`deviceId` as queryable fields) written by
 * RegisterGroupPushTokenUseCase on the Android client. It's a flat
 * collection rather than a `groups/{groupId}/push_tokens` subcollection
 * because the client's NetworkService only speaks flat two-segment
 * "collection/documentId" Firestore paths.
 */
export const sendGroupDrawNotification = onDocumentUpdated(
  "groups/{groupId}",
  async (event) => {
    const groupId = event.params.groupId;
    const before = event.data?.before.data();
    const after = event.data?.after.data();

    const wasDrawn = hasEntries(before?.draws);
    const isNowDrawn = hasEntries(after?.draws);

    if (wasDrawn || !isNowDrawn) {
      return;
    }

    await sendToGroupTokens(groupId, {
      title: "🎉 O sorteio foi realizado!",
      body: "Abra o app para conferir quem você tirou.",
    });
  }
);

/**
 * Notifies every registered device in a group (other than the sender) when
 * a new anonymous chat message arrives. The notification body is
 * intentionally generic — it never echoes the message text — to preserve
 * the anonymous-chat premise even on a lock-screen banner.
 */
export const sendChatMessageNotification = onValueCreated(
  "chats/{groupId}/{messageId}",
  async (event) => {
    const groupId = event.params.groupId;
    const message = event.data.val() as Record<string, unknown> | null;
    const senderId = typeof message?.si === "string" ? message.si : undefined;

    // "system" messages (e.g. "fulano acessou o chat") don't need a push.
    if (senderId === "system" || !senderId) {
      return;
    }

    await sendToGroupTokens(
      groupId,
      {
        title: "💬 Nova mensagem anônima",
        body: "Alguém deixou uma dica no chat do grupo.",
      },
      senderId
    );
  }
);

interface NotificationContent {
  title: string;
  body: string;
}

const PUSH_TOKENS_COLLECTION = "push_tokens";

async function sendToGroupTokens(
  groupId: string,
  content: NotificationContent,
  excludeDeviceId?: string
): Promise<void> {
  const tokensSnapshot = await firestore
    .collection(PUSH_TOKENS_COLLECTION)
    .where("groupId", "==", groupId)
    .get();

  if (tokensSnapshot.empty) {
    logger.info(`No push tokens registered for group ${groupId}`);
    return;
  }

  const staleTokenDocIds: string[] = [];

  const sends = tokensSnapshot.docs
    .filter((doc) => doc.get("deviceId") !== excludeDeviceId)
    .map(async (doc) => {
      const token = doc.get("token");
      if (typeof token !== "string" || token.length === 0) {
        return;
      }

      try {
        await messaging.send({
          token,
          notification: content,
        });
      } catch (error) {
        logger.warn(`Failed to send push to device ${doc.id}`, error);
        if (isUnregisteredTokenError(error)) {
          staleTokenDocIds.push(doc.id);
        }
      }
    });

  await Promise.all(sends);

  await Promise.all(
    staleTokenDocIds.map((docId) =>
      firestore
        .collection(PUSH_TOKENS_COLLECTION)
        .doc(docId)
        .delete()
        .catch((error) =>
          logger.warn(`Failed to clean up stale token ${docId}`, error)
        )
    )
  );
}

function hasEntries(value: unknown): boolean {
  return typeof value === "object" && value !== null && Object.keys(value).length > 0;
}

function isUnregisteredTokenError(error: unknown): boolean {
  const code = (error as { code?: string } | undefined)?.code;
  return code === "messaging/registration-token-not-registered";
}
