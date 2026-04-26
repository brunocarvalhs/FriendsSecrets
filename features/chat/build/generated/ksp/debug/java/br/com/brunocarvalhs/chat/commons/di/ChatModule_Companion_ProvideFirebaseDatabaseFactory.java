package br.com.brunocarvalhs.chat.commons.di;

import com.google.firebase.database.FirebaseDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class ChatModule_Companion_ProvideFirebaseDatabaseFactory implements Factory<FirebaseDatabase> {
  @Override
  public FirebaseDatabase get() {
    return provideFirebaseDatabase();
  }

  public static ChatModule_Companion_ProvideFirebaseDatabaseFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FirebaseDatabase provideFirebaseDatabase() {
    return Preconditions.checkNotNullFromProvides(ChatModule.Companion.provideFirebaseDatabase());
  }

  private static final class InstanceHolder {
    static final ChatModule_Companion_ProvideFirebaseDatabaseFactory INSTANCE = new ChatModule_Companion_ProvideFirebaseDatabaseFactory();
  }
}
