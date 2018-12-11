package com.doscan.qrcode;

import com.doscan.qrcode.standard.qrcode.InputThing;
import com.doscan.qrcode.standard.qrcode.NumberInputThing;
import com.doscan.qrcode.standard.qrcode.TextInputThing;

import java.util.ArrayList;

public class InputResolver {

    ArrayList<InputThing> inputThings = new ArrayList<>(8);

    public InputResolver(){
        inputThings.add(new NumberInputThing());
        inputThings.add(new TextInputThing());

    }
}
