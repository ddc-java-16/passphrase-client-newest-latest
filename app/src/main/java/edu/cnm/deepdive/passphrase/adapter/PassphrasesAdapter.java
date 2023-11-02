package edu.cnm.deepdive.passphrase.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.passphrase.R;
import edu.cnm.deepdive.passphrase.adapter.PassphrasesAdapter.Holder;
import edu.cnm.deepdive.passphrase.model.Passphrase;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class PassphrasesAdapter extends RecyclerView.Adapter<Holder>{

  private  final List<Passphrase> passphrases;
  private final LayoutInflater inflater;
  @ColorInt
  private final int evenRowColor;
  @ColorInt
  private final int oddRowColor;

  public PassphrasesAdapter(Context context, List<Passphrase> passphrases) {
    this.passphrases = passphrases;
    inflater = LayoutInflater.from(context);
    evenRowColor = context.getColor(R.color.even_row_color);
    oddRowColor = context.getColor(R.color.odd_row_color);
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new Holder(inflater.inflate(R.layout.item_passphrases, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {
holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return 0;
  }

  public  class Holder extends RecyclerView.ViewHolder {


    private Holder(
        @NonNull @NotNull View itemView) {
      super(itemView);
    }
    private void bind(int position) {
      ((TextView) itemView).setText(passphrases.get(position).getName());
      itemView.setBackgroundColor((position % 2 == 0) ? evenRowColor : oddRowColor);
    }
  }

}
