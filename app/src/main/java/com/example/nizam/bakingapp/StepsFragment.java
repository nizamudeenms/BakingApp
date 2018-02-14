package com.example.nizam.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepsFragment extends Fragment {

    @BindView(R.id.ingredient_list)
    ListView mListView1;

    @BindView(R.id.steps_list)
    ListView mListView2;

    private ArrayList<String> ingredientsArr = new ArrayList<String>();
    private ArrayList<String> stepsDescArr = new ArrayList<String>();
    private ArrayList<BakingSteps> tempStepsArr = new ArrayList<BakingSteps>();

    public ArrayList<String> getIngredientsArr() {
        return ingredientsArr;
    }

    public void setIngredientsArr(ArrayList<String> ingredientsArr) {
        this.ingredientsArr = ingredientsArr;
    }

    public ArrayList<String> getStepsDescArr() {
        return stepsDescArr;
    }

    public void setStepsDescArr(ArrayList<String> stepsDescArr) {
        this.stepsDescArr = stepsDescArr;
    }

    public ArrayList<BakingSteps> getTempStepsArr() {
        return tempStepsArr;
    }

    public void setTempStepsArr(ArrayList<BakingSteps> tempStepsArr) {
        this.tempStepsArr = tempStepsArr;
    }

    OnStepClickListener mCallback;

    public interface OnStepClickListener {
        void onStepSelected(Bundle dataBundle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement interface");
        }
    }

    public StepsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps, container, false);
        ButterKnife.bind(this, view);
        mListView1.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getIngredientsArr()));
        mListView2.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getStepsDescArr()));
        ListUtils.setDynamicHeight(mListView1);
        ListUtils.setDynamicHeight(mListView2);
        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                Bundle b = new Bundle();
                b.putString("bakingVideo", tempStepsArr.get(position).getVideoUrl().isEmpty() ? "nil" : tempStepsArr.get(position).getVideoUrl());
                b.putString("stepId", tempStepsArr.get(position).getStepId());
                b.putString("bakingId", tempStepsArr.get(position).getBakingId());
                b.putString("shortDesc", tempStepsArr.get(position).getShortDesc());
                b.putString("desc", tempStepsArr.get(position).getDesc());
                mCallback.onStepSelected(b);
            }
        });
        BakingAppWidgetUpdateService.startBakingService(getContext(), ingredientsArr);
        return view;
    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }

}
