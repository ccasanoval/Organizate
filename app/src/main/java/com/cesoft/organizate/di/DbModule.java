package com.cesoft.organizate.di;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;

import com.cesoft.organizate.db.DbOpenHelper;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@Module
public final class DbModule
{
	@Provides
	@Singleton
	SQLiteOpenHelper provideOpenHelper(Application app)
	{
		return new DbOpenHelper(app);
	}

	@Provides
	@Singleton
	SqlBrite provideSqlBrite()
	{
		//android.util.Log.e("DbModule","provideSqlBrite--------------------------------------------");
		return new SqlBrite.Builder().logger(
			new SqlBrite.Logger()
			{
				@Override public void log(String message)
				{
					Timber.tag("Database").v(message);
				}
			})
			.build();
	}

	@Provides
	@Singleton
	BriteDatabase provideDatabase(SqlBrite sqlBrite, SQLiteOpenHelper helper)
	{
		BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
		db.setLoggingEnabled(true);
		return db;
	}
}
