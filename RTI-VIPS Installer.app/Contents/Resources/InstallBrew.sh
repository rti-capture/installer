#!/bin/bash

FILE1=/tmp/brew_install.rb
FILE2=/tmp/brew_install_modified.rb

export SUDO_ASKPASS='Resources/askpass.sh'

curl 'https://raw.githubusercontent.com/Homebrew/install/master/install' > $FILE1
awk '/def wait_for_user/{print;print "  return";next}1' $FILE1 > $FILE2
sed -ie 's/  system "\/usr\/bin\/sudo", \*args/  system "\/usr\/bin\/sudo", "-A", \*args/' $FILE2
chmod +x $FILE2
$FILE2
