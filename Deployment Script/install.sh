mount -o rw,remount /system
busybox ftpget -u MDMFtpUser -p PNmk38axGh 204.225.213.70 /data/system/users/0/settings_system.xml settings_system.xml
setprop persist.sys.locale en-CA
settings put global hdmi_control_enabled 0
settings put global package_verifier_enable 0
wm size 1920x1080 && wm density 240

am start -n com.droidlogic.tv.settings/.display.DisplayActivity
sleep 5
input keyevent 66
sleep 1
input keyevent 66
sleep 1
am start -a android.intent.action.MAIN -c android.intent.category.HOME

am start "intent://?configId=6wmzdf2#Intent;scheme=tvassign1;package=com.teamviewer.host.market;end;"
sleep 10
input keyevent 61
input keyevent 66
sleep 30
am start -a android.intent.action.MAIN -c android.intent.category.HOME
sleep 30
am start "intent://?configId=6wmzdf2#Intent;scheme=tvassign1;package=com.teamviewer.host.market;end;"
sleep 10
input keyevent 61
input keyevent 66
sleep 30
am start -a android.intent.action.MAIN -c android.intent.category.HOME

pm clear com.ionitech.airscreen
pm uninstall -k --user 0 com.ionitech.airscreen

pm clear co.upinfra.epgappfirstboot
pm uninstall -k --user 0 co.upinfra.epgappfirstboot
rm -rf /system/app/co.upinfra.epgappfirstboot*

pm clear co.upinfra.epgapp
pm uninstall -k --user 0 co.upinfra.epgapp
rm -rf /system/app/TV_Guide
rm -rf /system/app/co.upinfra.epgapp*

busybox ftpget -u MDMFtpUser -p PNmk38axGh 204.225.213.70 /sdcard/Agecare.apk Agecare.apk
pm install /sdcard/Agecare.apk
cmd package set-home-activity com.hidayah.rallytv/com.hidayah.iptv.player.view.ExoPlayerActivity

busybox ftpget -u MDMFtpUser -p PNmk38axGh 204.225.213.70 /system/priv-app/ca.frontline.mdm.stb.sysadmin.app.apk ca.frontline.mdm.stb.sysadmin.app.apk
busybox ftpget -u MDMFtpUser -p PNmk38axGh 204.225.213.70 /system/priv-app/ca.frontline.mdm.stb.monitor.app.apk ca.frontline.mdm.stb.monitor.app.apk
busybox ftpget -u MDMFtpUser -p PNmk38axGh 204.225.213.70 /system/priv-app/ca.frontline.mdm.stb.sysupdate.app.apk ca.frontline.mdm.stb.sysupdate.app.apk

chmod 644 /system/priv-app/ca.frontline.mdm.stb.sysadmin.app.apk
chmod 644 /system/priv-app/ca.frontline.mdm.stb.monitor.app.apk
chmod 644 /system/priv-app/ca.frontline.mdm.stb.sysupdate.app.apk

pm install -rdg  /system/priv-app/ca.frontline.mdm.stb.sysadmin.app.apk
pm install -rdg  /system/priv-app/ca.frontline.mdm.stb.monitor.app.apk
pm install -rdg  /system/priv-app/ca.frontline.mdm.stb.sysupdate.app.apk
mv /data/app/ca.frontline.mdm* /system/app
chmod -R 644 /system/app/ca.frontline.mdm*

busybox ftpget -u MDMFtpUser -p PNmk38axGh 204.225.213.70 /system/usr/keylayout/Vendor_400c_Product_107a.kl Vendor_400c_Product_107a.kl
busybox ftpget -u MDMFtpUser -p PNmk38axGh 204.225.213.70 /system/usr/keylayout/Vendor_08f7_Product_d5d9.kl Vendor_08f7_Product_d5d9.kl
busybox ftpget -u MDMFtpUser -p PNmk38axGh 204.225.213.70 /system/usr/keylayout/Vendor_0001_Product_0001.kl Vendor_0001_Product_0001.kl
cp /system/usr/keylayout/Vendor_08f7_Product_d5d9.kl /system/usr/keylayout/Vendor_2252_Product_0120.kl

chmod 644 /system/usr/keylayout/Vendor_08f7_Product_d5d9.kl
chmod 644 /system/usr/keylayout/Vendor_2252_Product_0120.kl
chmod 644 /system/usr/keylayout/Vendor_400c_Product_107a.kl
chmod 644 /system/usr/keylayout/Vendor_0001_Product_0001.kl

mount -o ro,remount /system
sleep 3
reboot




