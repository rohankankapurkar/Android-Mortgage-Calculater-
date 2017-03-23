package com.lab2.lab2nav;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link calculatorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link calculatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class calculatorFragment extends Fragment {

    DbHelper mydb;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public  EditText resultText,aprText,loanText,downPaymentText = null;
    private String ores,oapr,oloan,odown =null;
    private RadioGroup radioGroup;
    private RadioButton years;
    private double apr;
    private int nper,pv,dpAmt;
    private View view;
    private String[] states;
    private Spinner spinner;
    public double res;


    private OnFragmentInteractionListener mListener;

    public calculatorFragment() {
        // Required empty public constructor
    }


    public static calculatorFragment newInstance(String param1, String param2) {
        calculatorFragment fragment = new calculatorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        states = getResources().getStringArray(R.array.states_list);
        view = inflater.inflate(R.layout.fragment_calculator, container, false);
        Button calculate = (Button) view.findViewById(R.id.calculate);
        spinner = (Spinner) view.findViewById(R.id.states);
        Spinner propertyType = (Spinner) view.findViewById(R.id.propertyType);
        resultText = (EditText) view.findViewById(R.id.result);
        aprText = (EditText) view.findViewById(R.id.aprValue);
        loanText =(EditText) view.findViewById(R.id.loanAmt);
        radioGroup=(RadioGroup)view.findViewById(R.id.radioGroup);
        downPaymentText = (EditText) view.findViewById(R.id.downPayment);


        mydb = new DbHelper(getActivity());


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, states);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);


        ArrayAdapter<String> propAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.property_type));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        propertyType.setAdapter(propAdapter);



        calculate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                calculateMortgage();
            }
        });

        Button clear = (Button)view.findViewById(R.id.newC);
        clear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                clearInput();
            }

        });

        Button save = (Button) view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveInput();
            }

        });


        return view;

    }

    private void saveInput()
    {
        Spinner propertyType = (Spinner) view.findViewById(R.id.propertyType);
        EditText streetAddress = (EditText) view.findViewById(R.id.address);
        EditText cityName = (EditText) view.findViewById(R.id.city);
        EditText zipcode = (EditText) view.findViewById(R.id.zipcode);
        Spinner mySpinner=(Spinner) view.findViewById(R.id.states);

        String property =  propertyType.getSelectedItem().toString();
        String street =  streetAddress.getText().toString();
        String city =  cityName.getText().toString();
        String state =  mySpinner.getSelectedItem().toString();
        String zip =  zipcode.getText().toString();



        mydb.insertData(property, street, city, state, zip,pv,apr,res);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(oapr,aprText.getText().toString());
        outState.putString(oloan,loanText.getText().toString());
        outState.putString(odown,downPaymentText.getText().toString());
        outState.putString(ores,resultText.getText().toString());


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        resultText = (EditText) getActivity().findViewById(R.id.result);
        aprText = (EditText) getActivity().findViewById(R.id.aprValue);
        loanText =(EditText) getActivity().findViewById(R.id.loanAmt);
        radioGroup=(RadioGroup)getActivity().findViewById(R.id.radioGroup);
        downPaymentText = (EditText) getActivity().findViewById(R.id.downPayment);

        if(savedInstanceState != null)
        {
            aprText.setText(savedInstanceState.getString(oapr));
            loanText.setText(savedInstanceState.getString(oloan));
            downPaymentText.setText(savedInstanceState.getString(odown));
            resultText.setText(savedInstanceState.getString(ores));
        }



    }

    private void calculateMortgage()
    {

        if(validateInput())
        {
            apr = apr / 1200;
            nper = nper * 12;
            pv =pv - dpAmt;
            double power = Math.pow((1 + apr), nper);
            res = (pv * power * apr) / (power - 1);
            res = Math.round(res * 100.0) / 100.0;
            resultText.setText(Double.toString(res));
        }
        else
        {
            resultText.setText("");
        }
    }

    private Boolean validateInput()
    {
        boolean flag;
        if (aprText.getText().toString().length() == 0 )
        {
            aprText.setHint("Enter the APR");
            aprText.setError("APR is mandatory");
            flag = false;
        }
        else
        {
            apr = Double.parseDouble(aprText.getText().toString());
            flag = true;
        }
        if (loanText.getText().toString().length() == 0 )
        {
            loanText.setError("Loan amount is mandatory");
            loanText.setHint("Enter the Loan Amount");
            flag = false;
        }
        else
        {
            pv = Integer.parseInt(loanText.getText().toString());
            flag = true;
            if(downPaymentText.getText().toString().length() == 0)
            {
                dpAmt =0;
            }
            else {

                dpAmt =Integer.parseInt(downPaymentText.getText().toString());
                if (dpAmt < pv) {
                    pv = pv - dpAmt;
                    flag = true;
                } else {
                    Toast.makeText(getActivity(), "Down payment cannot be greater than the Loan Amount", Toast.LENGTH_LONG).show();
                    flag = false;

                }
            }


        }

        if(radioGroup.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(getActivity(), "Please select the term(yrs)", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else
        {
            int selectedId=radioGroup.getCheckedRadioButtonId();
            years=(RadioButton) view.findViewById(selectedId);
            nper = Integer.parseInt(years.getText().toString().substring(0,2));
            flag = true;
        }

        Log.d(TAG, Boolean.toString(flag));
        return flag;
    }


    private void clearInput()
    {
        ViewGroup group = (ViewGroup)view.findViewById(R.id.calcView);
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }
        }
        radioGroup.clearCheck();

    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
