package app.rstone.com.contactsapp.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

public class PhoneUtil {
    private Context __this;
    private Activity act;
    private String phoneNumber;

    public PhoneUtil(Context __this, Activity act) {
        this.__this = __this;
        this.act = act;
    }

    public void dial() {
        __this.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
    }

    public void call() {
        if (ActivityCompat.checkSelfPermission(__this,Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(act,new String[]{Manifest.permission.CALL_PHONE},
                    2);
        }else{
            __this.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber)));
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
