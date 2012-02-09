#!/bin/bash
aws put --public partymoodmeter/pmm.apk android_app/bin/android_app.apk
echo http://s3.amazonaws.com/partymoodmeter/pmm.apk
