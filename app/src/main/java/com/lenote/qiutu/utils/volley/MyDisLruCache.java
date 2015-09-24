package com.lenote.qiutu.utils.volley;

import android.content.Context;
import android.util.Log;

import com.jakewharton.disklrucache.DiskLruCache;
import com.lenote.qiutu.BuildConfig;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by lenote on 2015/9/10.
 */
public class MyDisLruCache {
    private static final int APP_VERSION = 1;
    private static final int VALUE_COUNT = 1;
    private static int IO_BUFFER_SIZE = 8*1024;
    private DiskLruCache mDiskCache;

    public MyDisLruCache(Context context,String uniqueName,int diskCacheSize) {
        try {
            mDiskCache = DiskLruCache.open(
                    getDiskCacheDir(context, uniqueName),
                    APP_VERSION,
                    VALUE_COUNT,
                    diskCacheSize
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private File getDiskCacheDir(Context context, String uniqueName) {
        final String cachePath = context.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }

    public void put(String key,byte[] data){
        DiskLruCache.Editor editor = null;
        try {
            editor = mDiskCache.edit( key );
            if ( editor == null ) {
                return;
            }

            if( writeToFile(data, editor) ) {
                mDiskCache.flush();
                editor.commit();
                if ( BuildConfig.DEBUG ) {
                    Log.d("cache_test_DISK_", "image data put on disk cache " + key);
                }
            } else {
                editor.abort();
                if ( BuildConfig.DEBUG ) {
                    Log.d( "cache_test_DISK_", "ERROR on: image data put on disk cache " + key );
                }
            }
        } catch (IOException e) {
            if ( BuildConfig.DEBUG ) {
                Log.d( "cache_test_DISK_", "ERROR on: image put on disk cache " + key );
            }
            try {
                if ( editor != null ) {
                    editor.abort();
                }
            } catch (IOException ignored) {
            }
        }
    }

    public byte[] get( String key ) {

        DiskLruCache.Snapshot snapshot = null;
        byte[] data = null;
        try {
            snapshot = mDiskCache.get( key );
            if ( snapshot == null ) {
                return null;
            }
            final InputStream in = snapshot.getInputStream( 0 );
            if ( in != null ) {
                final BufferedInputStream buffIn =
                        new BufferedInputStream( in, IO_BUFFER_SIZE );
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int count;
                while ((count = buffIn.read(buffer))!= -1){
                    baos.write(buffer,0,count);
                }
                data = baos.toByteArray();
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            if ( snapshot != null ) {
                snapshot.close();
            }
        }
        return data;
    }


    private boolean writeToFile(byte[] data, DiskLruCache.Editor editor )
            throws IOException{
        OutputStream out = null;
        try {
            out = new BufferedOutputStream( editor.newOutputStream( 0 ), IO_BUFFER_SIZE );
            out.write(data);
            return true;
        }finally {
            if ( out != null ) {
                out.close();
            }
        }
    }
}
