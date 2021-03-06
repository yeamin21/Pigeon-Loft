package com.hoyt.pigeondb.pairs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hoyt.pigeondb.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PairsAdapter extends RecyclerView.Adapter<PairsAdapter.PairsHolder> {
    @NonNull
    Context c;
    ArrayList<Pairs> p;


    public PairsAdapter(@NonNull Context c, ArrayList<Pairs> p) {
        this.c = c;
        this.p = p;
    }


    @Override
    public PairsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pair_cardpair, parent, false);
        PairsHolder p = new PairsHolder(v);
        return p;
    }

    @Override
    public void onBindViewHolder(@NonNull PairsHolder holder, int position) {

        holder.bindpairs(p.get(position));
    }

    @Override
    public int getItemCount() {
        return p.size();
    }

    public class PairsHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView tm, tf;
        EditText date_picker;
        RecyclerView rV;
        Date d1;
        CardView cardView;
        Button arrowBtn, add_new_egg, show_breeding;
        ConstraintLayout constraintLayout;
        Calendar cl;
        int mYear, mDay, mMonth;
        DatabaseReference rf;
        String HatchingProbableDate;

        public PairsHolder(@NonNull View itemView) {


            super(itemView);

            this.itemView = itemView;
            tm = itemView.findViewById(R.id.txt_fPg);
            tf = itemView.findViewById(R.id.txt_maleP);
            rV = itemView.findViewById(R.id.rView);
            arrowBtn = itemView.findViewById(R.id.arrowBtn);
            cardView = itemView.findViewById(R.id.cardView);
            date_picker = itemView.findViewById(R.id.txt_date_picker);
            cl = Calendar.getInstance();
            mYear = cl.get(Calendar.YEAR);
            mMonth = cl.get(Calendar.MONTH);
            mDay = cl.get(Calendar.DAY_OF_MONTH);
            add_new_egg = itemView.findViewById(R.id.add_new_egg);
            show_breeding = itemView.findViewById(R.id.btn_breedingInfo);


        }


        void bindpairs(final Pairs pn) {

            tf.setText(pn.getFatherPGN());
            tm.setText(pn.getMotherPGN());
            rf = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid()).child("Pairs").child(pn.getPairsKey()).child("Breeding");
            date_picker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatePickerDialog(c, new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            SimpleDateFormat simpleDateFormat=   new SimpleDateFormat("dd-MM-YYYY");

                            Calendar cc=Calendar.getInstance();
                            cc.set(year,month,dayOfMonth);
                            String date=simpleDateFormat.format(cc.getTime());
                            cc.add(Calendar.DATE,18);
                            HatchingProbableDate =simpleDateFormat.format(cc.getTime());
                            date_picker.setText(date);
                        }
                    }, mYear, mMonth, mDay).show();



                }
            });
            constraintLayout = itemView.findViewById(R.id.constraintLayout);

            arrowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (constraintLayout.getVisibility() == View.GONE) {

                        constraintLayout.setVisibility(View.VISIBLE);


                    } else if (constraintLayout.getVisibility() == View.VISIBLE) {

                        constraintLayout.setVisibility(View.GONE);

                    }


                }
            });

            show_breeding.setOnClickListener(new View.OnClickListener() {
                ArrayList<Eggs> eg = new ArrayList<>();

                @Override
                public void onClick(View v) {

                    Intent i = new Intent(itemView.getContext(), BreedingTab.class);
                    i.putExtra("PAIR", pn.getPairsKey());
                    itemView.getContext().startActivity(i);

                }
            });

            add_new_egg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String layingDate = date_picker.getText().toString();
                    Eggs eg=new Eggs();

                    eg.setLaying(layingDate);
                    eg.setStatus("Expected");
                    eg.setHatching(HatchingProbableDate);
                    rf.push().setValue(eg);
                }
            });


        }


    }


}
