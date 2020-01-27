package com.example.macbookpro.myapplication.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macbookpro.myapplication.Adapter.ReclamationAdapter;
import com.example.macbookpro.myapplication.Beans.Reclamation;
import com.example.macbookpro.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailReclamationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailReclamationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailReclamationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseUser user;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mRef;
    private String userID;



    private OnFragmentInteractionListener mListener;
    private View v;
    private ArrayList<Reclamation> arrayList;
    private ListView mListView;
    private TextView pasRectamation;

    public DetailReclamationFragment() {
        // Required empty public constructor
    }

    public static DetailReclamationFragment newInstance(String param1, String param2) {
        DetailReclamationFragment fragment = new DetailReclamationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_detail_reclamation, container, false);
        mListView = (ListView) v.findViewById(R.id.reclamationList);
        pasRectamation = (TextView) v.findViewById(R.id.pas_reclamation_textview);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();
        user = mAuth.getCurrentUser();
        if (user != null){
            userID = user.getUid();
        }

        getData();

        return v;
    }

    private void getData(){
        String userString;

        if (user!=null){
            DatabaseReference myRef = mRef.child(userID);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Log.d("Tag", "Value is: " + dataSnapshot.getChildrenCount());
                    showData(dataSnapshot);
                    pasRectamation.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("tag","Erreur");

                }
            });
        }
        else{
            Log.d("Tag","user is null");
            pasRectamation.setText("pour consulter les reclamations vous devez etre connecté!");
        }
    }


    private void showData(DataSnapshot snapshot){

        arrayList = new ArrayList<Reclamation>();
        for(DataSnapshot ds : snapshot.getChildren()){
            Reclamation reclamation = new Reclamation();
            reclamation.setCategorie(ds.getValue(Reclamation.class).getCategorie());
            reclamation.setMessage(ds.getValue(Reclamation.class).getMessage());
            reclamation.setRue(ds.getValue(Reclamation.class).getRue());
            reclamation.setType(ds.getValue(Reclamation.class).getType());

            arrayList.add(reclamation);
            Log.d("TagReclamation",reclamation.getCategorie());

        }


        ReclamationAdapter mAdapter = new ReclamationAdapter(getActivity(),arrayList);
        mListView.setAdapter(mAdapter);


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
