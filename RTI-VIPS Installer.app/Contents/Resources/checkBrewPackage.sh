#!/bin/bash

BREW=/usr/local/bin/brew

$BREW list -1 | grep -q ^$1$
