#!/bin/bash

FILE="/tmp/.com.apple.dt.CommandLineTools.installondemand.in-progress"

export SUDO_ASKPASS='Resources/askpass.sh'

touch $FILE
PROD=$(softwareupdate -l |
  grep "\*.*Command Line" |
  head -n 1 | awk -F"*" '{print $2}' |
  sed -e 's/^ *//' |
  tr -d '\n') 
sudo -A softwareupdate -i "$PROD" -v;
rm -f $FILE
