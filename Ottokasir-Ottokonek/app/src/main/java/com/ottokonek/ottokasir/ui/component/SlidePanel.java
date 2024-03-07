package com.ottokonek.ottokasir.ui.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ottokonek.ottokasir.R;

public class SlidePanel {

    public static View createPanel(Context context, final ViewGroup attachedRoot, int resourcePanelID) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view = (View) inflater.inflate(resourcePanelID,attachedRoot,false);

    attachedRoot.addView(view);
        final ViewGroup panelSlide = view.findViewById(R.id.panel_slide);
        final ViewGroup panelItem = view.findViewById(R.id.content_dropdown);

//        Animation bottomUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
//        panelSlide.startAnimation(bottomUp);

//        panelSlide.setVisibility(View.VISIBLE);
        View contentView = inflater.inflate(R.layout.content_panel_transaction,panelItem,false);

        panelItem.addView(contentView);

        return view;
    }

    public View clearPanel(final ViewGroup attachedRoot){
        attachedRoot.removeAllViews();
        return null;
    }
}
