package edu.cnm.deepdive.passphrase.viewmodel;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.passphrase.model.Passphrase;
import edu.cnm.deepdive.passphrase.service.PassphraseRepository;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import java.util.List;
import javax.inject.Inject;
import org.jetbrains.annotations.NotNull;

@HiltViewModel
public class PassphraseViewModel extends ViewModel implements DefaultLifecycleObserver {
  private final PassphraseRepository repository;
  private final MutableLiveData<Passphrase> passphrase;
  private final MutableLiveData<List<Passphrase>> passphrases;
  private final MutableLiveData<List<String>> generated;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  @Inject
  PassphraseViewModel(@ApplicationContext Context context, PassphraseRepository repository) {

    this.repository = repository;
    passphrase = new MutableLiveData<>();
    passphrases = new MutableLiveData<>();
    generated = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    fetch();
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

  public MutableLiveData<List<String>> getGenerated() {
    return generated;
  }

  public void fetch(String key) {
    execute(repository.get(key), passphrase::postValue, throwable);


  }

  public void fetch() {
    execute(repository.get(), passphrases::postValue, throwable);

  }

  public void search(String fragment) {
    execute(repository.search(fragment), passphrases::postValue);

  }

  public void add(Passphrase passphrase) {
    execute(repository
        .add(passphrase), this.passphrase::postValue, throwable);
  }
  public void update(Passphrase passphrase) {
    execute(repository
        .replace(passphrase), this.passphrase::postValue, throwable);
  }
  private void postThrowable(Throwable throwable) {
    Log.e(getClass().getSimpleName(), throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

  public void delete(String key) {
    repository
        .delete(key)
        .subscribe(() -> {}, this::postThrowable, pending);
  }

  public void generate(int length) {
    execute(repository.generate(length), generated::postValue);
  }

  public void clearGenerated() {
    generated.postValue(null);
  }

  public void  clearPassphrase() {
    passphrase.postValue(null);
  }

  @Override
  public void onStop(@NotNull LifecycleOwner owner) {
    DefaultLifecycleObserver.super.onStop(owner);
    pending.clear();
  }


  private <T> void execute(Single<T> stream, Consumer<? super T> consumer, MutableLiveData<?>... clearList) {
    clearLiveData(clearList);
    stream.subscribe(consumer, this::postThrowable, pending);

  }

  private void clearLiveData(MutableLiveData<?>[] clearList) {
    throwable.postValue(null);
    for (MutableLiveData<?> ld : clearList) {
      ld.postValue(null);
    }
  }

  private void execute(Completable stream, Action action, MutableLiveData<?>... clearList) {
 clearLiveData(clearList);
 stream.subscribe(action, this::postThrowable, pending);
    }
  }

