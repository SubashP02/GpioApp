package com.example.ledscroll;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ledscroll.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'ledscroll' library on application startup.
    static {
        System.loadLibrary("ledscroll");
    }

    private native String sendToJNI(String selectedItem, int inputText, int value);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = findViewById(R.id.spinner);
        EditText editTextInput = findViewById(R.id.editText_input);
        TextView textViewResult = findViewById(R.id.textView_result);
        Button buttonon = findViewById(R.id.button_send);
        Button buttonoff = findViewById(R.id.button_send2);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        Map<String, String> itemMap = new HashMap<>();
        itemMap.put("gpiochip 1", "/dev/gpiochip1");
        itemMap.put("gpiochip 2", "/dev/gpiochip2");
        itemMap.put("gpiochip 3", "/dev/gpiochip3");
        itemMap.put("gpiochip 4", "/dev/gpiochip4");
        itemMap.put("gpiochip 5", "/dev/gpiochip5");
        itemMap.put("gpiochip 6", "/dev/gpiochip6");
        itemMap.put("gpiochip 7", "/dev/gpiochip7");
        itemMap.put("gpiochip 8", "/dev/gpiochip8");
        itemMap.put("gpiochip 9", "/dev/gpiochip9");
        itemMap.put("gpiochip 10", "/dev/gpiochip10");


        buttonon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedItem = spinner.getSelectedItem().toString();
                String mappedValue = itemMap.get(selectedItem);
                String inputTextString = editTextInput.getText().toString();
                int inputText = Integer.parseInt(inputTextString);
                String result = sendToJNI(mappedValue, inputText, 1);
                textViewResult.setText(result);
            }
        });
                buttonoff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String selectedItem = spinner.getSelectedItem().toString();
                        String mappedValue = itemMap.get(selectedItem);
                        String inputTextString = editTextInput.getText().toString();
                        int inputText = Integer.parseInt(inputTextString);
                        String result = sendToJNI(mappedValue, inputText, 0);
                        textViewResult.setText(result);
                    }
                }
        );
    }
}