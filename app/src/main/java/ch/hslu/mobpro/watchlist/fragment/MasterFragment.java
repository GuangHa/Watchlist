package ch.hslu.mobpro.watchlist.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.hslu.mobpro.watchlist.R;

public class MasterFragment extends Fragment {

    private Callbacks callbacks;

    public static MasterFragment newInstance() {
        return new MasterFragment();
    }

    public interface Callbacks {
        void onMasterItemClicked(int masterItemId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(!(context instanceof Callbacks)) {
            throw new RuntimeException("Context must implement callbacks");
        }
        callbacks = (Callbacks) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_master, container, false);

        final TextView homeTextView = (TextView) view.findViewById(R.id.master_item_1);
        final TextView searchTextView = (TextView) view.findViewById(R.id.master_item_2);
        final TextView watchlistTextView = (TextView) view.findViewById(R.id.master_item_3);
        final TextView settingsTextView = (TextView) view.findViewById(R.id.master_item_4);

        homeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightText(homeTextView, searchTextView, watchlistTextView, settingsTextView);
                callbacks.onMasterItemClicked(1);
            }
        });

        searchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightText(searchTextView, homeTextView, watchlistTextView, settingsTextView);
                callbacks.onMasterItemClicked(2);
            }
        });

        watchlistTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightText(watchlistTextView, searchTextView, homeTextView, settingsTextView);
                callbacks.onMasterItemClicked(3);
            }
        });

        settingsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightText(settingsTextView, searchTextView, watchlistTextView, homeTextView);
                callbacks.onMasterItemClicked(4);
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    private void highlightText(TextView textView, TextView noHighlightTextView, TextView noHighlightTextView2, TextView noHighlightTextView3) {
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        noHighlightTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        noHighlightTextView2.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        noHighlightTextView3.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
    }
}