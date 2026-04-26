package br.com.brunocarvalhs.chat.app.data.services;

import com.google.firebase.database.FirebaseDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class FirebaseRealtimeManager_Factory implements Factory<FirebaseRealtimeManager> {
  private final Provider<FirebaseDatabase> databaseProvider;

  private FirebaseRealtimeManager_Factory(Provider<FirebaseDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public FirebaseRealtimeManager get() {
    return newInstance(databaseProvider.get());
  }

  public static FirebaseRealtimeManager_Factory create(
      Provider<FirebaseDatabase> databaseProvider) {
    return new FirebaseRealtimeManager_Factory(databaseProvider);
  }

  public static FirebaseRealtimeManager newInstance(FirebaseDatabase database) {
    return new FirebaseRealtimeManager(database);
  }
}
