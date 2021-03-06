package com.example.nizam.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepsFragment extends Fragment implements BakingStepsAdapter.StepSelectedListener {

    @BindView(R.id.ingredient_list_recycler_view)
    RecyclerView ingredientsRecyclerView;

    @BindView(R.id.steps_list_recycler_view)
    RecyclerView stepsRecyclerView;

    BakingStepsAdapter mStepsAdapter;
    BakingIngredientsAdapter mIngredientsAdapter;
    private StepListener stepListener;


    public interface StepListener {
        void itemClicked(Bundle b);
    }



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



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            stepListener = (StepListener) context;
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


        RecyclerView.LayoutManager mLayoutManager2 = new GridLayoutManager(getContext(), 1);
        ingredientsRecyclerView.setLayoutManager(mLayoutManager2);
        ingredientsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ingredientsRecyclerView.setHasFixedSize(true);
        ingredientsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        mIngredientsAdapter = new BakingIngredientsAdapter(getIngredientsArr());
        ingredientsRecyclerView.setAdapter(mIngredientsAdapter);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        stepsRecyclerView.setLayoutManager(mLayoutManager);
        stepsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        stepsRecyclerView.setHasFixedSize(true);
        stepsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        mStepsAdapter = new BakingStepsAdapter(getContext(),getStepsDescArr(), tempStepsArr,this::onStepSelected);
        stepsRecyclerView.setAdapter(mStepsAdapter);
        BakingAppWidgetUpdateService.startBakingService(getContext(), ingredientsArr);
        return view;
    }

    @Override
    public void onStepSelected(int position) {
        Bundle b = new Bundle();
        b.putString("bakingVideo", tempStepsArr.get(position).getVideoUrl().isEmpty() ? "nil" : tempStepsArr.get(position).getVideoUrl());
        b.putString("stepId", tempStepsArr.get(position).getStepId());
        b.putString("bakingId", tempStepsArr.get(position).getBakingId());
        b.putString("shortDesc", tempStepsArr.get(position).getShortDesc());
        b.putString("desc", tempStepsArr.get(position).getDesc());
        b.putString("thumbnailUrl", tempStepsArr.get(position).getThumbnailUrl());
        stepListener.itemClicked(b);
    }
}
