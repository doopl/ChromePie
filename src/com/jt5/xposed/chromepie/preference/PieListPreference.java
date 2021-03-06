package com.jt5.xposed.chromepie.preference;

import android.content.Context;
import android.content.res.Resources;
import android.preference.ListPreference;
import android.util.AttributeSet;

import com.jt5.xposed.chromepie.R;

public class PieListPreference extends ListPreference {

    private static String PACKAGE_NAME;

    public PieListPreference(Context context) {
        super(context);
    }

    public PieListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieListPreference(Context context, int item, int slice) {
        super(context);
        initialise(item, slice);
        PACKAGE_NAME = getContext().getApplicationContext().getPackageName();
    }

    @Override
    public void setValue(String value) {
        String oldValue = getValue();
        super.setValue(value);
        if (!value.equals(oldValue)) {
            updateState(value);
            notifyDependencyChange(shouldDisableDependents());
        }
    }

    @Override
    public boolean shouldDisableDependents() {
        boolean shouldDisableDependents = super.shouldDisableDependents();
        String value = getValue();
        return shouldDisableDependents || value == null || value.equals("none");
    }

    private void initialise(int item, int slice) {
        setKey("slice_" + slice + "_item_" + item);
        setTitle("Pie Item " + item);
        if (item == slice) {
            setTitle(getTitle() + " (Main item)");
        }
        setEntries(R.array.pie_item_entries);
        setEntryValues(R.array.pie_item_values);
        setIcon(R.drawable.null_icon);
        setDefaultValue("none");
        setOrder(item);
    }

    private void updateState(String newValue) {
        final int index = findIndexOfValue(newValue);
        setSummary(getEntry());
        final Resources res = getContext().getResources();
        final String identifier = res.getStringArray(R.array.pie_item_dark_drawables)[index];
        setIcon(res.getIdentifier(identifier, "drawable", PACKAGE_NAME));
    }

}
