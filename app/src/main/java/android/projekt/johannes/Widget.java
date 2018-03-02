package android.projekt.johannes;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
/**
 * This class is for the Widget of the Application.
 * NOTICE: 
 * THE WIDGET IS JUST A HELP IF THE APPLICATION BREAKS DOWN (WHICH NORMALLY SHOULDN'T HAPPEN)
 * IF THE APP IS SHUT DOWN THE USER CAN START THE APP BY CLICKING ON THE WIDGET 
 * THE WIDGET IS ON EVERY "PAGE" OF THE TABLET
 * 
 * @author Aigner Johannes (Universität Regensburg: Medieninformatik/Informationswissenschaft)
 *
 */
public class Widget extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);
			Intent intent = new Intent(context, StartActivity.class);
			PendingIntent pendIntent = PendingIntent.getActivity(context, 0,
					intent, 0);
			remoteViews.setOnClickPendingIntent(R.id.widget_layout, pendIntent);
			appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
		}
	}

}
