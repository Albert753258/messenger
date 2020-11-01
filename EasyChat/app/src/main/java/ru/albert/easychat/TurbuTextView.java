package ru.albert.easychat;

import android.content.Context;
import android.text.Editable;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class TurbuTextView extends androidx.appcompat.widget.AppCompatTextView {

    public TurbuTextView(Context context) {
        super(context);
    }

    public TurbuTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TurbuTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
