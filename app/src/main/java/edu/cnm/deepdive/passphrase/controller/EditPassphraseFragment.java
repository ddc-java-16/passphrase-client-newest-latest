package edu.cnm.deepdive.passphrase.controller;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.passphrase.R;
import edu.cnm.deepdive.passphrase.databinding.FragmentEditPassphraseBinding;
import edu.cnm.deepdive.passphrase.model.Passphrase;
import edu.cnm.deepdive.passphrase.viewmodel.PassphraseViewModel;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

public class EditPassphraseFragment extends DialogFragment implements TextWatcher {

  private static final Pattern SPLITTER = Pattern.compile("\\W+");
  private FragmentEditPassphraseBinding binding;
  private PassphraseViewModel viewModel;
  private String key;
  private AlertDialog dialog;

  private Passphrase passphrase;

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    binding = FragmentEditPassphraseBinding.inflate(getLayoutInflater(), null, false);
    key = EditPassphraseFragmentArgs.fromBundle(getArguments()).getKey();
    dialog = new androidx.appcompat.app.AlertDialog.Builder(requireContext())
       // .setIcon()   //TODO: 11/3/23 Import suitable icon drawable .
        .setTitle(R.string.passphrase_details)
        .setView(binding.getRoot())
        .setPositiveButton(android.R.string.ok, (dlg, which) -> save())
        .setNegativeButton(android.R.string.cancel, (dlg, which) -> {/* DO NOTHING*/})
        .create();
    dialog.setOnShowListener((dlg) -> checkSubmitConditions());
    binding.name.addTextChangedListener(this);
    binding.words.addTextChangedListener(this);
    binding.generate.setOnClickListener((v) -> viewModel.generate(binding.length.getValue()));
    binding.length.setMinValue(2);
    binding.length.setMaxValue(10);
      return dialog;

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView (@NonNull @NotNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState){
      return binding.getRoot();
    }

    @Override
    public void onViewCreated (@NonNull @NotNull View view,
        @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState){
      super.onViewCreated(view, savedInstanceState);
      viewModel = new ViewModelProvider(requireActivity())
          .get(PassphraseViewModel.class);
      viewModel.clearGenerated();
      viewModel.getGenerated()
          .observe(getViewLifecycleOwner(), (words) -> binding.words.setText((words != null) ? String.join("\n", words) : ""));
        passphrase = new Passphrase();
      if (key != null) {
        viewModel
            .getPassphrase()
            .observe(getViewLifecycleOwner(), (passphrase) -> {
              if(passphrase != null) {
                binding.name.setText(passphrase.getName());
                binding.words.setText(String.join("\n", passphrase.getWords()));
                this.passphrase = passphrase;
              }
            });
        viewModel.fetch(key);
      } else {
      }
    }

    @Override
    public void onDestroyView () {
      binding = null;
      super.onDestroyView();
    }

    @Override
    public void beforeTextChanged (CharSequence s,int start, int count, int after){
      //DO NOTHING
    }

    @Override
    public void onTextChanged (CharSequence s,int start, int before, int count){
//DO NOTHING
    }

    private void save() {
    passphrase.setName(binding.name.getText().toString().strip());
    passphrase.clear();
        SPLITTER.splitAsStream(binding.words.getText().toString())
            .filter((word) -> !word.isEmpty())
            .forEach(passphrase::append);
        viewModel.add(passphrase);

    viewModel.add(passphrase);

    }

    @Override
    public void afterTextChanged (Editable s){
      checkSubmitConditions();
    }


    private void checkSubmitConditions () {
      dialog.getButton(DialogInterface.BUTTON_POSITIVE)
          .setEnabled(

              !binding.name.getText().toString().isBlank()
              && !binding.words.getText().toString().isBlank()
          );
    }

  }
