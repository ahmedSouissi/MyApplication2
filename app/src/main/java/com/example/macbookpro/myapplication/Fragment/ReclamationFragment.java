package com.example.macbookpro.myapplication.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.macbookpro.myapplication.Beans.Reclamation;
import com.example.macbookpro.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReclamationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReclamationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReclamationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_IMAGE = 100;
    private ImageView photo ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private String mCurrentPhotoPath;
    private Uri imageUri;
    private Button sendButton, effacerButton;
    private AutoCompleteTextView rueAutoCmp;
    private Button btnCategorie;
    private EditText messageEditText;
    private RadioGroup radioGroup;
    private View v;
    private FirebaseUser currentUser;
    private boolean photoChanged = false;


    public ReclamationFragment() {
        // Required empty public constructor
    }

    public static ReclamationFragment newInstance(String param1, String param2) {
        ReclamationFragment fragment = new ReclamationFragment();
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
        v = inflater.inflate(R.layout.fragment_reclamation, container, false);
        photo = (ImageView) v.findViewById(R.id.photo);
        photo.setImageResource(R.drawable.ic_menu_camera);
        photo.setOnClickListener(photoListener());
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser() ;
        sendButton = (Button) v.findViewById(R.id.send_button);
        sendButton.setOnClickListener(sendButtonListner());
        effacerButton = (Button) v.findViewById(R.id.reset_button);
        effacerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
        rueAutoCmp = (AutoCompleteTextView) v.findViewById(R.id.rue_auto_complete);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,getResources().getStringArray(R.array.Sayada));
        rueAutoCmp.setAdapter(adapter);
        rueAutoCmp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    rueAutoCmp.showDropDown();
                }
            }
        });
        messageEditText = (EditText) v.findViewById(R.id.message_edit_text);
        btnCategorie = (Button) v.findViewById(R.id.btn_categorie);
        btnCategorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSpinner();
            }
        });
        radioGroup = (RadioGroup) v.findViewById(R.id.radio_group);
        return v;
    }

    private View.OnClickListener sendButtonListner() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userId ;
                if (currentUser!=null){
                    userId = currentUser.getUid();
                }
                else{
                    userId = "anonyme";
                }

                String rue = rueAutoCmp.getText().toString();
                String message = messageEditText.getText().toString();
                String categorie = btnCategorie.getText().toString();
                if (!rue.isEmpty()&&!categorie.isEmpty()&&!message.isEmpty()){


                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    RadioButton selectedButton = (RadioButton) v.findViewById(selectedId);
                    String type = selectedButton.getText().toString();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    String id = database.getReference().child(userId).push().getKey() ;
                    DatabaseReference myRef = database.getReference().child(userId).child(id);
                    Reclamation reclamation = new Reclamation(rue,categorie,message,type);
                    myRef.setValue(reclamation).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()){
                                Log.d("tag","succée");
                                //TODO alert
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Succée");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                            else{
                                Log.d("tag","erreur");
                            }
                        }
                    });
                    if(photoChanged){

                        savePhotoFireBase(userId,id);
                    }
                }
                else{
                    if (rue.isEmpty()){
                        rueAutoCmp.setError("Veuillez remplir tous les champs !");
                        rueAutoCmp.requestFocus();
                    }
                    else if (categorie.isEmpty()){
                        btnCategorie.setError("Veuillez remplir tous les champs !");
                        btnCategorie.requestFocus();
                    }
                    else if (message.isEmpty()){
                        messageEditText.setError("Veuillez remplir tous les champs !");
                        messageEditText.requestFocus();
                    }
                    Toast.makeText(getActivity(),"Veuillez remplir tous les champs !",Toast.LENGTH_SHORT).show();
                }

            }
        };
    }

    private void savePhotoFireBase(String userId, String proclamationId){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(userId).child(proclamationId);
        photo.setDrawingCacheEnabled(true);
        photo.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) photo.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("tag","failure");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.d("Tag","Success");
            }});




    }

    private View.OnClickListener photoListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG","Galerie");
                final CharSequence[] items = {"Galerie","Prendre une photo"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(items[i].equals("Galerie")){

                            Toast.makeText(getActivity(),"Galerie",Toast.LENGTH_SHORT).show();
                            openGallery();

                        }
                        else{
                            Toast.makeText(getActivity(),"Prendre une photo",Toast.LENGTH_SHORT).show();
                            dispatchTakePictureIntent();

                        }

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        };

    }
    public void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI) ;
        startActivityForResult(gallery,PICK_IMAGE);}

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d("RECLAMATIONFRAGMENTAG", "Capture Ok");
            Bundle extras = data.getExtras();
            imageUri = data.getData();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photo.setImageBitmap(imageBitmap);
            photoChanged = true;
        }
        else if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            Log.d("RECLAMATIONFRAGMENTAG", "pickImage Ok");
            imageUri = data.getData() ;
            saveImage(imageUri);
        }
    }


    public void saveImage(Uri uriImage){
        InputStream imageStream = null ;
        try {
            imageStream = getActivity().getContentResolver().openInputStream(uriImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
        photo.setImageBitmap(imageBitmap);
        photoChanged = true;
    }

    private void reset(){
        btnCategorie.setTextColor(Color.rgb(128,128,128));
        btnCategorie.setText("Choisissez la nature de reclamation/propsition");
        messageEditText.getText().clear();
        rueAutoCmp.getText().clear();
        if (photoChanged){
            photo.setImageResource(R.drawable.ic_menu_camera);
            photoChanged = false;
        }
    }

    private void showSpinner(){
        /*final ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.reclamation_cate));
        final Spinner spinner = new Spinner(getActivity());
        spinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        spinner.setAdapter(adp);*/
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choisissez la nature de reclamation");
        builder .setItems(R.array.reclamation_cate, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String[] items = getResources().getStringArray(R.array.reclamation_cate);
                btnCategorie.setText(items[i]);
                btnCategorie.setTextColor(Color.BLACK);
            }
        });
        builder.create().show();

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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
