#!/bin/bash

FILE1=/tmp/brew_install.rb
FILE2=/tmp/brew_install_modified.rb
BREW_CACHE=/Library/Caches/Homebrew

SCRIPTDIR=`echo "$(cd "$(dirname "$BASH_SOURCE")"; pwd)"`

export SUDO_ASKPASS="$SCRIPTDIR/askpass.sh"

if [ -e $BREW_CACHE ] && [ ! -w $BREW_CACHE ]; then
  echo "Problem:"
  echo ""
  echo "Lack of write access to Homebrew cache directory. This can be caused by"
  echo "another user performing a non-system install of Homebrew."
  echo ""
  echo "Please do these commands in the Terminal:"
  echo ""
  echo "  sudo chown -R root:admin $BREW_CACHE"
  echo "  sudo chmod -R g+w $BREW_CACHE"
  exit 1
fi

curl 'https://raw.githubusercontent.com/Homebrew/install/master/install' > $FILE1
awk '/def wait_for_user/{print;print "  return";next}1' $FILE1 > $FILE2
sed -ie 's/  system "\/usr\/bin\/sudo", \*args/  system "\/usr\/bin\/sudo", "-A", \*args/' $FILE2
chmod +x $FILE2
$FILE2
