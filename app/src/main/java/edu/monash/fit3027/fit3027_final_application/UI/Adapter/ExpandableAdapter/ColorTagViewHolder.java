package edu.monash.fit3027.fit3027_final_application.UI.Adapter.ExpandableAdapter;

import android.content.Context;
import android.graphics.Color;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.google.android.gms.common.data.DataBuffer;

import edu.monash.fit3027.fit3027_final_application.Helper.DatabaseHelper;
import edu.monash.fit3027.fit3027_final_application.R;
import edu.monash.fit3027.fit3027_final_application.model.ColorTag;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

/**
 * Created by YunHao Zhang
 * Student ID: 26956047
 * A view holder for colorTag
 */

public class ColorTagViewHolder extends ParentViewHolder implements View.OnLongClickListener, TextView.OnEditorActionListener{

    private TextView categoryEditText;
    private ImageView colorTagCatImageView;
    private ImageView arrowImageView;
    private String colorHex;
    private Context mContext;
    private DatabaseHelper DBHelper;

    /**
     * Construct a colorTagViewHolder
     * @param itemView An item view
     */
    public ColorTagViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        categoryEditText = (TextView) itemView.findViewById(R.id.categoryEditText);
        colorTagCatImageView = (ImageView) itemView.findViewById(R.id.colorTagCatImageView);
        arrowImageView = (ImageView) itemView.findViewById(R.id.arrowImageView);
        DBHelper = new DatabaseHelper(mContext);
        categoryEditText.setOnEditorActionListener(this);

    }

    /**
     * Bind the colorTag to item view
     * @param colorTag
     */
    public void onBind(ColorTag colorTag){
        categoryEditText.setText(colorTag.getDescription());
        //Make the editText not editable
        categoryEditText.setOnLongClickListener(this);
        categoryEditText.setMaxLines(1);

        categoryEditText.setFocusable(false);
        categoryEditText.setClickable(true);
        categoryEditText.clearFocus();

        colorHex = colorTag.getColorHex();
        colorTagCatImageView.setBackgroundColor(Color.parseColor(colorHex));
    }

    private void animateExpand() {
        //Rotate the arrow icon 360
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrowImageView.startAnimation(rotate);
    }

    private void animateCollapse() {
        //Rotate the arrow icon 360
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrowImageView.startAnimation(rotate);
    }

    /**
     * Called when a view has been clicked and held.
     *
     * @param v The view that was clicked and held.
     *
     * @return true if the callback consumed the long click, false otherwise.
     */
    @Override
    public boolean onLongClick(View v) {
        categoryEditText.setFocusable(true);
        categoryEditText.setEnabled(true);
        categoryEditText.setFocusableInTouchMode(true);
        categoryEditText.requestFocus();
        //Make editText editable
        return true;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        //If user pressed enter, then save the content
        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_GO ||
                actionId == EditorInfo.IME_ACTION_NEXT ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            InputMethodManager inputManager =
                    (InputMethodManager) mContext.
                            getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                    v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            categoryEditText.setFocusable(false);
            categoryEditText.setClickable(true);
            categoryEditText.clearFocus();
            //Make it uneditable
            DBHelper.updateColor(colorHex, categoryEditText.getText().toString());
            return true; // consume.

        }
        return false; // pass on to other listeners.
    }


    public void onExpansionToggled(boolean expanded) {
        if (expanded) { // rotate clockwise
            animateExpand();
        } else { // rotate counterclockwise
            animateCollapse();
        }
    }

}
