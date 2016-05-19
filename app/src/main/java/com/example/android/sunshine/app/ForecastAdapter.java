package com.example.android.sunshine.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter
{
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE = 1;

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /**
     * Returns the view type use for a list item
     *
     * @param position - position in the listview
     * @return int
     */
    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        // get layout from viewType var
        switch (viewType) {
            case VIEW_TYPE_TODAY:
                layoutId = R.layout.list_item_forecast_today; break;
            case VIEW_TYPE_FUTURE:
                layoutId = R.layout.list_item_forecast; break;
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        int weatherId = cursor.getInt(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID));

        int viewType = getItemViewType(cursor.getPosition());
        int imageResourceId = -1;
        // get layout from viewType var
        switch (viewType) {
            case VIEW_TYPE_TODAY:
                imageResourceId = Utility.getArtResourceForWeatherCondition(weatherId); break;
            case VIEW_TYPE_FUTURE:
                imageResourceId = Utility.getIconResourceForWeatherCondition(weatherId); break;
        }
        viewHolder.iconView.setImageResource(imageResourceId);

        long msDate = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        viewHolder.dateView.setText(Utility.getFriendlyDayString(mContext, msDate));

        String description = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        viewHolder.descView.setText(description);

        Boolean isMetric = Utility.isMetric(context);

        float maxTemp = cursor.getFloat(ForecastFragment.COL_WEATHER_MAX_TEMP);
        viewHolder.maxTempView.setText(Utility.formatTemperature(context, maxTemp, isMetric));

        float minTemp = cursor.getFloat(ForecastFragment.COL_WEATHER_MIN_TEMP);
        viewHolder.minTempView.setText(Utility.formatTemperature(context, minTemp, isMetric));
    }

    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView descView;
        public final TextView maxTempView;
        public final TextView minTempView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            descView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            dateView = (TextView) view.findViewById(R.id.list_item_forecast_date);
            maxTempView = (TextView) view.findViewById(R.id.list_item_forecast_high);
            minTempView = (TextView) view.findViewById(R.id.list_item_forecast_low);
        }
    }
}