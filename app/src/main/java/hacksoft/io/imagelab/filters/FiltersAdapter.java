package hacksoft.io.imagelab.filters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Transformation;

import java.lang.reflect.Constructor;
import java.util.List;

import hacksoft.io.imagelab.R;
import hacksoft.io.imagelab.Util;

public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.ViewHolder> {
    private static final String TAG = FiltersAdapter.class.getSimpleName();

    private static final List<Class<? extends Transformation>> filters = Util.getFilters();

    public Transformation newFilterInstance(Context context, int index) {
        Class<? extends Transformation> filterClass = filters.get(index);
        try {
            Constructor<? extends Transformation> c = filterClass.getConstructor(Context.class);
            return c.newInstance(context);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    private ListItemClickListener listener;
    public FiltersAdapter(ListItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView card = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_transformation, parent, false);
        return new ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView textView = (TextView) holder.card.findViewById(R.id.text_transformation);
        final Class<? extends Transformation> t = filters.get(position);
        textView.setText(t.getSimpleName().replace("FilterTransformation", ""));
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    public interface ListItemClickListener {
        void onFilterItemClick(int clickedItemIndex);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView card;
        public ViewHolder(CardView card) {
            super(card);
            this.card = card;
            this.card.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onFilterItemClick(getAdapterPosition());
        }
    }
}
