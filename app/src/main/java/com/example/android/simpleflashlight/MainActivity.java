package com.example.android.simpleflashlight;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

/**
 * Controller class for activity_main layout
 * The proposal for the activity is to present a toggle button for turning on and off the camera flash light
 */
public class MainActivity
        extends AppCompatActivity
        implements
        DialogInterface.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    // Client for the camera service
    private Camera camera;

    // Flag that indicates if the camera flash is on
    private boolean isFlashOn;

    // Flag that determinates if the camera has flash feature available
    private boolean hasFlash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call super method
        super.onCreate(savedInstanceState);
        // Inflates then content layout with this controller
        setContentView(R.layout.activity_main);

        // check if the device supports flashlight
        hasFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {
            // if the device does not support flashlight, show an alert dialog and close app

            // Creation from the builder class
            AlertDialog alert = new AlertDialog.Builder(this).create();
            // Title
            alert.setTitle(getString(R.string.error_title_message));
            // Messsage
            alert.setMessage(getString(R.string.flashlight_feature_not_supported_message));
            // This alert dialog is not cancelable if user touches outside the dialog
            alert.setCanceledOnTouchOutside(Boolean.FALSE);
            // This alert dialog is not cancelable if user presses back button
            alert.setCancelable(Boolean.FALSE);
            // Sets the OK button
            alert.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok_label), this);
            // Shows the alert dialog to the user
            alert.show();
        }

        // Inflates switch button
        ToggleButton flashSwitch = (ToggleButton) findViewById(R.id.flash_switch);

        // Sets the onckeckedchange listener
        flashSwitch.setOnCheckedChangeListener(this);

    }

    /**
     * Gets a camera object claiming resource to the global field camera object
     */
    private void getCamera() {
        // Checks if the resource is already assigned to this client object
        if (camera == null) {
            // Creates a new Camera object to access the first back-facing camera on the
            // device. If the device does not have a back-facing camera, this returns null.
            camera = Camera.open();
        }
    }

    /**
     * Turns on the flash light
     */
    private void turnOnFlash() {
        // Checks if the light is not already turned on
        if (!isFlashOn) {
            // checks if the camera object has the resource
            if (camera == null) {
                // If there is no resource, do nothing
                return;
            }
            // Change the camera client parameter for flash mode, is in this step where
            // actually we turn on the camera flashlight
            camera.getParameters().setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            // Then we update our flag
            isFlashOn = Boolean.TRUE;
        }
    }

    /**
     * Turns off the flash light
     */
    private void turnOffFlash() {
        // Checks if the light is not already turned off
        if (isFlashOn) {
            // Checks if the camera object has the resource
            if (camera == null) {
                // If there is no resource, do nothing
                return;
            }
            // Updates the paremeter, so the light is now turned off
            camera.getParameters().setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            // Updates the parameter
            isFlashOn = Boolean.FALSE;

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // If the activity becomes paused, we turn off the flash
        turnOffFlash();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // If the activity is started, wheter if the activity is first-time created or the activity
        // comes from paused stated, we claim the camera resource
        getCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // If the activity is stopped, release resource
        if (camera != null) {
            // Disconnects and releases the Camera object resources.
            camera.release();
            // prepares object to GC
            camera = null;
        }
    }

    /**
     * This method will be invoked when a button in the dialog is clicked.
     *
     * @param dialog The dialog that received the click.
     * @param which  The button that was clicked (e.g.
     *               {@link DialogInterface#BUTTON1}) or the position
     *               of the item clicked.
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        // Pressing OK activity finishes
        finish();
    }

    /**
     * Called when the checked state of the compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            // If the new state is "checked", tur on flash
            turnOnFlash();
        } else {
            // If the new state is "unchecked", turn off flash
            turnOffFlash();
        }
    }
}
