package com.example.addiction;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class SlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SlideAdapter(Context context) {
        this.context = context;
    }

    public int[] slideImage = {

            R.drawable.iconphone,
            R.drawable.icontimer,
            R.drawable.iconrun,
            R.drawable.iconbooks,
            R.drawable.iconparty,
            R.drawable.iconperson,
    };

    public String[] slideHeading = {
            "Who?",
            "",
            "",
            "When?",
            "",
            "",
    };

    public String[] slideText = {
            "This app helps you to refrain yourself from using your phone.",
            "You can choose the time duration.",
            "At your own pace",
            "Use the app when you're studying",
            "or in a social event...",
            "or any time at all.",

    };

    @Override
    public int getCount() {
        return slideHeading.length;
    }


    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;

    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int i) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, viewGroup, false);

        ImageView sliderImageView = view.findViewById(R.id.imageView);
        TextView sliderHeadingView = view.findViewById(R.id.textView2);
        TextView sliderTextView = view.findViewById(R.id.textView);

        sliderImageView.setImageResource(slideImage[i]);
        sliderHeadingView.setText(slideHeading[i]);
        sliderTextView.setText(slideText[i]);

        viewGroup.addView(view);

        return view;

    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int i, Object o) {
        viewGroup.removeView((ConstraintLayout) o);

    }
}