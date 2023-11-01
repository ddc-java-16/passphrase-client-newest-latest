package edu.cnm.deepdive.passphrase.viewmodel;

import android.content.Context;
import android.support.v4.app.INotificationSideChannel.Default;
import android.util.Log;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.passphrase.model.Passphrase;
import edu.cnm.deepdive.passphrase.service.PassphraseRepository;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import java.util.List;
import javax.inject.Inject;
import org.jetbrains.annotations.NotNull;

@HiltViewModel
public class PassphraseViewModel extends ViewModel implements DefaultLifecycleObserver {
  private final PassphraseRepository repository;
  private final MutableLiveData<Passphrase> passphrase;
  private final MutableLiveData<List<Passphrase>> passphrases;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  @Inject
  PassphraseViewModel(@ApplicationContext Context context, PassphraseRepository repository) {

    this.repository = repository;
    passphrase = new MutableLiveData<>();
    passphrases = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
  }
//INCLUDE METHODS THAT CAN BE INVOKED FROM CONTROLLER FOR CRUD
  public MutableLiveData<Passphrase> getPassphrase() {
    return passphrase;
  }

  public MutableLiveData<List<Passphrase>> getPassphrases() {
    return passphrases;
  }

  public MutableLiveData<Throwable> getThrowable() {
    return throwable;
  }

  private void postThrowable(Throwable throwable) {
    Log.e(getClass().getSimpleName(), throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

  @Override
  public void onStop(@NotNull LifecycleOwner owner) {
    DefaultLifecycleObserver.super.onStop(owner);
    pending.clear();
  }
}