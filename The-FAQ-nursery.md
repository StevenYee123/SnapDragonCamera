# Meta-topics

_The meta-questions describe the role of this document. The questions in this section should *not* be copied to the forum FAQs._

**Q: Can I edit this document?**

Sure. The only things you really need to know is that:

1. If you want your changes to go live immediately then you need to contact Victor (@vchong). We haven't fully automated the flow from the nursery to the forums so we need the ping for anything urgent.

2. If the question does not yet have an adequate answer, perhaps because you're still thinking about it or because you want to encourage someone else to write one, then make sure to add '(DRAFT)' somewhere in the question. Draft questions are never copied to the forum. 

**Q: What is the FAQ nursery?**

One meaning of nursery is to describe a business that grows young plants until they are strong enough to be sold in a garden centre. In the same way that a nursery nurtures immature plants until they are ready to face the world, the FAQ nursery allows FAQs to be collaboratively edited (in markdown format) whilst they are not ready to be shared on the forum.

The nursery is, by design, not the place to come for complete (or even coherent) answers. It is a place to come to write, propose and savagely edit our frequently-asked-questions.

**Q: How should I phrase questions?**

FAQs are presented as questions and answers and it is important that the question is a question, rather than a title. It should read as a full English sentence that requests information.

**Q: What is the right tone (of language) for answers?**

A FAQ is *not* documentation and need not adopt the formal approach commonly found in technical documentation. Instead answers in a FAQ should adopt the more informal tone we expect to find on the forum. 

# General

## How to use the forum 

**Q: I'm new here. What should I read first?**

Reading the blog series "96Boards Out of box experience guide" is a great introduction to 96Boards:

* [96Boards Out of box experience guide – part 1](http://www.96boards.org/blog/96boards-box-experience-guide-1/)
* [96Boards Out of box experience guide – part 2](http://www.96boards.org/blog/96boards-box-experience-guide-2/)
* [96Boards Out of box experience guide – part 3](http://www.96boards.org/blog/96boards-box-experience-guide-3/)

**Q: What is the best way to ask questions on the forum?**

Firstly, consider checking to see if anyone else has asked a similar question. I'd recommend using a full search engine since that is more powerful than the search box built into the forum, for example if you use google just add <code>site:96boards.org your search words</code> to limit you search to the forums (and the 96Boards documentation).

This is the way to search from the mailing list. <code>site:lists.96boards.org your search words</code>

Otherwise the best thing to do is to start a new topic and give it a good title. The title appears in the side bar on the forum and gets lots of visibility. A good title will tend to attract better answers.

**Q: Can I add attachments to my forum posts?**

The forum does not allow attachments but it does allow you to embed links and images to externally hosted content.

For text files use something like [termbin](http://termbin.com/), [pastebin](http://pastebin.com/) or a [github gist](https://gist.github.com/).

For photos and images you can use any image host that allows external linking. I personally like [flickr](https://www.flickr.com/) because there's nothing the [terms of service](https://policies.yahoo.com/us/en/yahoo/terms/utos/) regarding user generated content that I find objectionable (though you must make your own mind up about that). Its also useful to be able to easily copy n’ paste links to the forum from the flickr share button.

Adding the link directly on the forum as bellow is fine.

`https://flic.kr/p/EZQq6K`

**Q: Is there a mailing list for 96Boards? Is the discussion in the mailing list saved somewhere?**

Yes, we have setup a mailing list for advanced software developers working on open source software on 96Boards.

* [List info and subscription management](https://lists.96boards.org/mailman/listinfo/dev)
* [Archive of the mailing list](https://lists.96boards.org/pipermail/dev/)

**Q: Why must I constantly prove that I am not a robot?**

*All* forums have to fight spam and are usually forced to make a choice between the costs of fighting spam reactively (constant clean up) and the cost of fighting spam pro-actively (CAPTCHAs, approvals requires for first few posts).

The CAPTCHA system used here at 96Boards is relatively lightweight, especially compared tests that require you to decode obfuscated text. It asks you to show you are not a robot by clicking on a tick box. Occasionally it will also ask you to solve a picture puzzle. Either way, we believe that this type of CAPTCHA has a good chance of success on the first try and we really hope it doesn't slow you down too much.

**Q: Why does CAPTCHA system refuse to let me to select pictures?**

The CAPTCHA system is provide for us by Google. Sometimes regions or companies block Google services at the firewall. We are considering to use other system but at the moment, to access the forum you need to have access to Google products.

Also we have received reports that the CAPTCHA system may occasionally become out-of-sync with the forum itself if the user has been logged into the forum long time. We're aware of the problem but is hard to reproduce and so it's taking a while to get it fixed. As a workaround try logging out, closing your web browser and reconnecting to the forum again.

# Common technical questions

**Q: Can I use the micro USB cable to power my board?**

Consumer Edition and Enterprise Edition 96Boards do not draw system power from their micro-USB sockets. To read why we decided not to permit this please take a look at:

* [96Boards Power Accessories](https://www.96boards.org/products/accessories/power/)

**Q: What type of AC adapter do I need?**

This question is important as using the wrong method to powering the 96Boards risks damage to your board.
Please refer the link to buy appropriate AC adapter with the right DC plug.

* [How to select the correct AC adapter](https://www.96boards.org/products/accessories/power/)

**Q: How do I turn on the 96Boards? Where is the power button?**

This is a good question since most of the regular PC has a power button to turn on/off power supply which is controlled by the motherboard.

The way to turn on all of the current 96Boards is just plug in your AC adapter to the DC jack and it will automatically start booting the bootloader and OS.

There is no power LED on the 96Boards, so there will be no indication if the 96Boards has been powered or not at the glance.

It starts to boot the bootloader but please wait for some time until screen shows up on the HDMI display.

If you have connected the serial console, watching the boot messages are also good indication of booting.

There are 4 LED between the two USB Type A connector and some of them will start flushing after OS has booted.

**Q: How to program devices connecting to GPIO/I2C on 96Boards?**

One of the great value of 96Boards is connecting sensors and devices and programming LED, relay, motors and etc to create robotics, cameras, set-top-boxes, wearables, medical devices, vending machines, building automation, industrial control, digital signage, and casino gaming consoles.

Reading the blog series is a great introduction to program GPIO/I2C devices on 96Boards:

* [Programming GPIO from bash](http://www.96boards.org/blog/96boards-box-experience-guide-4/)
* [Programming GPIO using libsoc](http://www.96boards.org/blog/96boards-box-experience-guide-5/)
* [Programing I2C devices with libmraa and libupm](http://www.96boards.org/blog/programing-i2c-devices-libmraa-libupm/)

**Q: Which Mezzanine boards I should buy? (DRAFT)**

Excellent question. I hope this will be a guide for selection.

+ If you would like only to have serial console feature the USB-UART adapter is for you.
  - [USB-UART adapter](http://www.96boards.org/products/mezzanine/uarts/)
+ If you would like to connect sensors and devices through GPIO and I2C, Grove Sensor Mezzanine should suite you. This also has the USB serial console built-in and you do not have to buy USB-UART adapter separately.
  - [Grove Sensor Mezzanine](http://www.96boards.org/products/mezzanine/sensors-mezzanine/)
  - [Example programs for controlling sensors](https://github.com/96boards/Starter_Kit_for_96Boards)

# Using Android on 96Boards

**Q: Can I use mouse/keyboard and adb at the same time?**

Very good question, it would be great if it could you the mouse/keyboard and adb at the same time.
The current 96Boards has two USB Type A connectors (the two big regular USB connectors) and one micro USB connector.
To use adb from your host PC, you need to connect the Host PC with micro USB connector.

Not all SoC used on the 96Boards have more than one USB OTG ports. The Dragonboard 410C and HiKey are sharing one USB port with switch between USB Type-A ports and micro USB ports. With this design it is not possible to use both USB Type-A connector and micro USB connector at the same time.

You need to unplug anything it is connected on the the USB Type A connector, such as, mouse or keyboard and etc, to use micro USB connector for adb.

Using any mouse/keyboard and other USB devices on regular usage of Android is perfectly fine,
just please remove any USB devices on both Type-A connector before inserting micro USB cable for starting to use adb.

**Q: Why does Android showing on the display disappears and stops?**

When booting the Android and the screen goes to blank after about a minute, it scars you as if the board have died.

This is standard behaviour of the Android just going into the sleep, as usual Android phones and tablets.
To awake the Android, inserting the keyboard and typing any key should go out from the sleep mode.

**Q: How to collect debug log on Android when having the issue?**

Happy for the question. The detail procedure to reproduce the issue and the debug log are essential for reporting the problem.

Just do:
```
adb shell dmesg > dmesg.log
adb logcat -d 2>&1 > logcat.log
```
Attach both dmesg.log and logcat.log.

# Hikey FAQ

**Q: Where should I look for documentation?**

* [HiKey Documentation](https://www.96boards.org/documentation/ConsumerEdition/HiKey/README.md/). This is a great starting point for hunting down installing OS images, board manuals, schematics and all the other documentation we require 96Boards vendors to put together.

**Q: What is the difference between the J15 and J601 boot mode pins?**

There are two versions of the Hikey, one is produced by LeMaker and the other by CircuitCo.

The boot mode pins are marked J601 on the LeMaker HiKey and located on the edge of the boards close to LS connector.
On the CircuitCo Hikey these pins are marked J15 and are located at almost the same location. You might need magnifier to see them since they are printed on the board very small.

The J15 and J601 are identical in function, it is only the silk screen that differs between the two boards.

This jumper pins are used to select the boot-time behaviour of the HiKey and are usually used to FLASH the bootloader. The pinout is the same regardless of the board manufacturer.

For booting Debian or AOSP automatically:

<pre>
Name          | Link     | State
------------- | -------- | ------
Auto Power up | Link 1-2 | closed
Boot Select   | Link 3-4 | open
GPIO3-1       | Link 5-6 | open
</pre>

For flushing OS into emmc with recovery mode:

<pre>
Name          | Link     | State
------------- | -------- | ------
Auto Power up | Link 1-2 | closed
Boot Select   | Link 3-4 | closed
GPIO3-1       | Link 5-6 | open
</pre>


**Q: Which UART to be able to see serial console screen, /dev/ttyAMA0 or AMA3?**

This is the same topic of regarding between UART0 and UART3.

There are pins on the HiKey printed as J801 on LeMaker and J16 on CircuitCo. Both are the same UART port for the HiKey.

The J16-J801 is connected on UART0 on the SoC of the HiKey and we were assigning the UART0 for sending and receiving data of serial console. The UART0 is recognized by the HiKey as the /dev/ttyAMA0.

The initial images of bootloader and kernel were built to use UART0 for the serial console.

However, to be able of users of 96Boards to use the serial console feature across the different 96Boards, we have decided to use UART3 on LS connector for the serial console instead of the UART0 port. The UART3 is recognized by the HiKey as the /dev/ttyAMA3.

The images from 15.11 release, the UART3 (/dev/ttyAMA3) works as the serial console.

Please refer the link bellow to use the serial console on HiKey.

* [Using serial console](http://www.96boards.org/forums/topic/short-intro-to-start-your-hikey-with-serial-console/)

**Q: Does Hikey support system suspend-to-RAM?**

It does! Although before intiating the suspend it is import to specific a wake up source, otherwise it is impossible to restart the board. The wake up source can either be the Real Time Clock (RTC) or one of the GPIO pins on the low-speed connector.

Note: _UART and USB wake up is not possible for HiKey. This is part of the design of the SoC and we don't see any way to workaround that._

1. For RTC wake up, you must enable CONFIG_RTC_DRV_PL031. This is supported by default on mainline kernel. Once the board has booted you can use the following command to request a wake up alarm (in this case the alarm will expire after 10 seconds):
~~~
echo +10 > /sys/class/rtc/rtc0/wakealarm; echo mem > /sys/power/state
~~~

2. To use the GPIO pins as a wake up signal use the following script as a reference:
~~~
# 488: GPIO2_0, 490: GPIO2_2,
# 492: GPIO2_4, 495: GPIO2_7
# GPIO2_1, GPIO2_3 busy

echo 488 > /sys/class/gpio/export
echo 490 > /sys/class/gpio/export
echo 492 > /sys/class/gpio/export
echo 495 > /sys/class/gpio/export

echo in > /sys/class/gpio/gpio488/direction
echo both > /sys/class/gpio/gpio488/edge
echo in > /sys/class/gpio/gpio490/direction
echo both > /sys/class/gpio/gpio490/edge
echo in > /sys/class/gpio/gpio492/direction
echo both > /sys/class/gpio/gpio492/edge
echo in > /sys/class/gpio/gpio495/direction
echo both > /sys/class/gpio/gpio495/edge
~~~

**Q: What is the link for Android Open Source Project (AOSP) support on HiKey?**

96Boards Hikey is officially supported in the upstream AOSP sources. Please visit android.com for details.

* [Selecting Devices for AOSP](https://source.android.com/source/devices.html)

**Q: Where is the git tree of the AOSP kernel source for Hikey?**

The kernel source for AOSP on Hikey is hosted on the googlesource.com.

* [https://android.googlesource.com/kernel/hikey-linaro/](https://android.googlesource.com/kernel/hikey-linaro/)

**Q: I would like to be a contributor for the AOSP on HiKey, what is the procedure?**

We would like to have people contribute to the AOSP on Hikey as much as possible.
Please refer the procedure.

* [Becoming contributor of AOSP on Hikey](http://www.96boards.org/forums/topic/how-to-becoming-contributor-of-aosp-on-hikey/)

**Q: Where is the vanilla kernel source (not AOSP) hosted for HiKey?** (DRAFT)

The sources used for Debian builds are host on the github.

* [https://github.com/96boards-hikey/linux](https://github.com/96boards-hikey/linux)

**Q: Is this it? There are no other frequently-asked-questions?**

This is all we've come up with for the HiKey. However if you browse over to [the General forum](http://www.96boards.org/forums/forum/general/) you'll find a FAQ covering questions of a general nature. That includes questions about the 96Boards project, as well as technical questions whose answers apply to more than one board.

# Becoming contributor of AOSP on Hikey

## Prerequisites

* Familiarize yourself with Gerrit; here are some links:
  * https://wiki.linaro.org/Platform/Android/Gerrit
  * https://wiki.openstack.org/wiki/Gerrit_Workflow 

* Make sure you have an account on https://android-review.googlesource.com/ and are properly added to the contributors agreement.

* Get a http password:
  * Log in to android-review.googlesource.com
  * Click on your name on the upper right, then settings.
  * Click on HTTP password, then obtain password.
  * Copy the string to a script "login-googlesource.sh".
  * Make the the script executable "chmod u+x login-googlesource.sh".

* Accept the Contributor Agreement, if necessary 

## Get the latest AOSP kernel tree

* Run the following commands:
```
$ export AOSP=hikey
$ export BRANCH=master 
$ export LOCAL=mychanges 
$ git clone https://android.googlesource.com/device/linaro/hikey 
$ cd $AOSP 
$ git checkout origin/$BRANCH -b $LOCAL
```

## Commit your changes

Patch, git add and git commit everything you want. Your patches should at least pass scripts/checkpatch.pl, as usual

## Add a Change-Id

Gerrit wants each logical change to have a Change-Id. This allows future versions of the same change (maybe modified due to review feedback) to have the same reference hash.

Get the sha1 list, you will need them on the next step:
```
$ git log --pretty=o origin/${BRANCH}..HEAD > ../sha1.list  
```

Rebase your changes to rework commit messages and add manual Change-Id tags for each patch:
```
$ git rebase -i origin/$BRANCH  
```

Change all the "pick"s to "reword" or just "r"

Then

* Above the "Signed-off-by:" lines, add:
```
Change-Id: I<original sha1sum for change from shalist> 
```

* Note the I is the prefix on the sha1sum. 

## Push the changes

Review all changes, making sure each has a unique Change-Id.

And finally, run
```
$ ./login-googlesource.sh
$ git push origin $LOCAL:refs/for/$BRANCH 
```
Then it will show the out put like this.
```
Counting objects: 22, done.
Delta compression using up to 4 threads.
Compressing objects: 100% (4/4), done.
Writing objects: 100% (4/4), 527 bytes | 0 bytes/s, done.
Total 4 (delta 3), reused 0 (delta 0)
remote: Resolving deltas: 100% (3/3)
remote: Processing changes: new: 1, done
remote:
remote: New Changes:
remote:   https://android-review.googlesource.com/[numbers] Title-of-your-patch
remote:
To https://android.googlesource.com/device/linaro/hikey
 * [new branch]      mychanges -> refs/for/master 
```

Visit the link showing above:

`https://android-review.googlesource.com/[numbers]`

And add following reviewers:
* john.stultz
* guodong.xu
* vishal.bhoj
  * (all three persons have the same @linaro.org address)

Have fun!

(This documentation was referred from https://wiki.linaro.org/Process/PushingBitsToAndroid)

# Dragonboard 410c FAQ

**Q: Where should I look for documentation?**

There's a wealth of documentation for the Dragonboard 410c. So much that it is quite possible that need a bit of help finding which document is the best one for you to read! If you can't find what you're looking for, feel free to ask on the forum. Usually someone will come along with a "hello" and a link!. However... if you're in a hurry let us offer you some ideas:

* [DragonBoard 410c Documentation (at 96boards.org)](https://www.96boards.org/documentation/ConsumerEdition/DragonBoard-410c/README.md/). This is a great starting point for hunting down board manuals, schematics and all the other documentation we require 96Boards vendors to put together (and, in the case of DragonBoard, a whole lot more besides).
* [DragonBoard 410c Tools and Resources by Qualcomm](https://developer.qualcomm.com/hardware/dragonboard-410c/tools). This is Qualcomm's own page about the board. It contains several documents not found in this site and, if Windows 10 IoT is your thing then this is where you go to get it.

**Q: How to boot after buying the DragonBoard 410c?**

Probably this is the first topic you would like to know. Please refer the instruction to boot your board with HDMI display.

* [Getting started with HDMI display](http://www.96boards.org/forums/topic/short-into-to-start-your-dragonboard-410c-with-hdmi-display/)

**Q: How to use serial console on 96Boards?**

The access to the board with serial console is essential for most for the developers. We have made the tutorial describing one of the easiest ways to get started with the serial console.

* [Getting started with serial console on Dragonboard 410c](http://www.96boards.org/forums/topic/short-into-to-start-your-dragonboard-410c-with-serial-console/)

**Q: What is the best source of information about Windows 10 IoT for DragonBoard 410c?**

Please refer the link to obtain the information of Windows IoT on DragonBoard 410C.

* [How to Setup Windows 10 IoT Core on Dragonboard](http://www.96boards.org/forums/topic/windows-10/)

**Q: How do I use GPS with the debian-derived software images?**

We've been looking at GPS in the debian-based images, and it's now working "in the lab". It's not yet integrated into the builds (not even the snapshot builds) but we have posted instructions on the forum. Our goal is to have everything merged into our build by the end of June. The final solution may be slightly different, but the steps posted to the forum are known to work...

* http://www.96boards.org/forums/topic/gps-software/#post-14390

**Q: Why USB is slow on my DragonBoard 410C?**

The old releases had an issue with USB throughput. The issue was fixed from 15.11 release.
Please update your image to the latest image from following link.

* [Choose your Build, Operating system, and Install Method](http://www.96boards.org/documentation/ConsumerEdition/DragonBoard-410c/Installation/README.md/)

**Q: Why the WiFi connection drops out periodically?**

The work around for this issue is included from 16.06 and later releases
and should not have the WIFI connection drops.

Bellow is for the record only.

---------- 

Yes, we are aware of the WIFI being disconnected periodically and in the middle of fixing the symptom.

This is the link in bug tracking system.
* [Bug 272 - periodic 10-15sec wifi connectivity loss](https://bugs.96boards.org/show_bug.cgi?id=272)

You may try the temporary workaround described at the bellow link until it is fixed.

* [WIFI workaround](http://www.96boards.org/forums/topic/ssh-and-ping-workarounds/)

----------

**Q: What is changed in the new release and how to customize the Linux kernel by building from source?**

There are great descriptions of what have improved on the new releases and known problem with detail instruction to build the Linux kernel from source.

* [Documentation by Linaro Qualcomm Landing Team](http://builds.96boards.org/releases/dragonboard410c/linaro/debian/latest/)

**Q: Why the DragonBoard 410C does not recognized by adb on windows?**

The usb driver for adb on windows requires modification from Android web site.
We have made the revised usb driver. Please try this driver.

* [adb usb driver windows DB410C](https://github.com/96boards/documentation/raw/master/ConsumerEdition/DragonBoard-410c/Downloads/Files/adb_usb_driver_windows_DB410C.zip)

**Q: How to install adb usb driver on Windoes for DragonBoard 410C?**

This is the instruction to install modified usb driver for adb on windows.

* [Step by step instruction to install adb usb driver on windows](http://www.96boards.org/forums/topic/step-by-step-instruction-to-install-adb-usb-driver-on-windows/)

**Q: How can I find the right application notes published in pdf?**

There are many great application notes uploaded on developer.qualcomm.com.
We have made a center page linking to the pdf files with the distinction of the contents.

* ["How to" documents on Qualcomm Developer Network](http://www.96boards.org/documentation/ConsumerEdition/DragonBoard-410c/AdditionalDocs/README.md/)

**Q: Where can I find the case?**

There are nice cases made by Arrows.

* [DragonBoard 410c Case Clear](http://www.chip1stop.com/web/USA/en/dispDetail.do?partId=ARRD-0000043&cid=dragonboard410c)

* [DragonBoard 410c Case Blue](http://www.chip1stop.com/web/USA/en/dispDetail.do?partId=ARRD-0000040&cid=dragonboard410c)

**Q: Is this it? There are no other frequently-asked-questions?**

This is all we've come up with for the DragonBoard 410c. However if you browse over to [the General forum](http://www.96boards.org/forums/forum/general/) you'll find a FAQ covering questions of a general nature. That includes questions about the 96Boards project, as well as technical questions whose answers apply to more than one board.

# Step by step instruction to install adb usb driver on Windows

## Start from opening Device Manager on Windows

Refer the link and open the Device Manager.
* [How to open Windows Device Manager](http://www.computerhope.com/issues/ch000833.htm)

## Connect Dragonboard 410C to Windows PC

There are two USB Type A connectors (the two big regular USB connectors) and one micro USB connector.
To use adb from your Windows PC, you need to connect the Host PC with micro USB connector.

You need to unplug anything it is connected on the the USB Type A connector, such as, mouse or keyboard and etc, to use micro USB connector for adb.

## Download adb usb driver for Windows

We have uploaded revised adb usb driver which will recognize DragonBoard 410C.

* <a href="https://github.com/96boards/documentation/raw/master/ConsumerEdition/DragonBoard-410c/Downloads/Files/adb_usb_driver_windows_DB410C.zip"> adb usb driver windows DB410C</a>

## Installing adb usb driver on Windows

After confirming the DragonBoard 410C is connected with Windows PC with microUSB cable, turn on the DragonBoard by connecting AC adaptoer to it.

The Device Manager on Windows should show this screen.

![](https://farm8.staticflickr.com/7691/28039568542_0ac17e9a7d_z_d.jpg)

![](https://farm8.staticflickr.com/7733/27528300213_ec6f79725a_z_d.jpg)

![](https://farm8.staticflickr.com/7211/28143779825_292a18c39d_z_d.jpg)

![](https://farm8.staticflickr.com/7298/27528300123_e34263e1c0_z_d.jpg)

![](https://farm8.staticflickr.com/7324/28039568362_d4825d15db_z_d.jpg)

![](https://farm8.staticflickr.com/7301/28039568452_1e00ec9011_z_d.jpg)

![](https://farm8.staticflickr.com/7283/28143780045_6582009ab0_z_d.jpg)


# Bubblegum-96 FAQ

**Q: Where is the documentation for the Bubblegum-96?**

We're busy working on it. In the mean take a look at https://github.com/96boards-bubblegum/linaro-adfu-tool to see how to FLASH different operating systems onto your Bubblegum-96. You can also come and join us at http://www.96boards.org/forums/forum/products/bubblegum96/ and ask for what you need.

**Q: How do I de-brick my board?**

There are two levels of recovery possible. The choice depends on whether or not the bootloader is functioning correctly. In both cases the recovery process is similar. The full debrick involves loading u-boot into RAM and running commands to re-FLASH the board. The lightweight debrick is simpler because u-boot can load itself into RAM.

Both recovery methods require you to have a 1.8v UART adapter (the [96Boards-UART](https://www.seeedstudio.com/item_detail.html?p_id=2525) and, conveniently is configured by default to connect to LS-UART1, however any other 1.8v adapter should be fine) and a USB type-A to type-A cable.

The full debrick process is described in the [Bubblegum-96 documentation](http://www.96boards.org/documentation/ConsumerEdition/Bubblegum-96/Installation/LinuxFastboot.md/).

For the quick recovery process, follow the debrick proceed from the `[Device (96board)]` section of step #6 (instead of running the host commands it is enough just to apply power to the board and mash the Enter key  button until the `owl> ` prompt appears. You should now be able to follow step #7 onwards as normal.

**Q: Where can I buy the USB type A to USB type A needed to debrick the board?**

It can be difficult to find these cables using a search engines because the cable you need tends to be hidden behind all the USB type A to type B (or micro-B) cables. Often its better just to browse the web site of a specialist cable provider.

To help you save a bit of time we've collected together links to suppliers who appear to sell suitable products. We haven't tested them all ourselves but hope its useful. The list is a bit shorter than we'd like right now so feel free to post links to alternative suppliers on [the forums](http://www.96boards.org/forums/forum/products/bubblegum96/) and we'll try to add them here.

* [Lindy USB type A plug to micro-B socket (UK)](http://www.lindy.co.uk/cables-adapters-c1/usb-c449/usb-2-0-c450/usb-2-0-type-a-male-to-micro-b-female-adapter-p8431/)

**Q: Is this it? There are no other frequently-asked-questions?**

This is all we've come up with for the Bubblegum-96. However if you browse over to [the General forum](http://www.96boards.org/forums/forum/general/) you'll find a FAQ covering questions of a general nature. That includes questions about the 96Boards project, as well as technical questions whose answers apply to more than one board.

# Lemaker Cello

**Q: Why is this section empty?**

The board is still being tested and enabled. Not many people have Cello's on their desks at this point so no questions have been frequently asked!