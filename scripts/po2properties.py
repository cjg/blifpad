#!/usr/bin/python
"""
    po2properties.py
    
    BlueLan -- <project_description>
  
    Copyright (C) 2006 - 2007
        Giuseppe Coviello <cjg@cruxppc.org>
  
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.
  
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
  
    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
"""

import sys

def escape (text):
    return text.replace (" ", "\ ").replace (":", "\:").replace ("'", "\'")

def po2properties (filein=None, fileout=None):
    fdin = None
    fdout = None

    if not filein:
        fdin = sys.stdin
    if not fileout:
        fdout = sys.stdout
    try:
        if not fdin:
            fdin = file (filein)
    except:
        sys.stderr.write ("Input file %s not found!\n" % filein)
        sys.exit (2)
    try:
        if not fdout:
            fdout = file (fileout, "w")
    except:
        sys.stderr.write ("Cannot open output file %s for writing!\n" % fileout)
        sys.exit (3)

    lines = fdin.readlines ()
    fdin.close ()
    msgid = ""
    for line in lines:
        if line[0] == "\"":
            fdout.write (line)
            continue
        if line[:5] == "msgid":
            msgid = escape (line[7:-2])+"="
            continue
        if line[:6] == "msgstr":
            msgstr = line[8:-2]
            if not len (msgstr):
                msgid = "!"+msgid
            fdout.write (msgid+msgstr+"\n")
            continue
        fdout.write (line)
    fdout.close ()

def usage (exit_code=1):
    print "Usage: ./po2properties.py [INPUTFILE] [OUTPUTFILE]"
    print "Converts po gettext file into Java properties format."
    print "-h, --help    This help screen"
    print
    print "Report bugs to <cjg@cruxppc.org>"
    sys.exit (exit_code)
    
if __name__ == "__main__":
    if "-h" in sys.argv or "--help" in sys.argv:
        usage (0)
    if len (sys.argv) == 1:
        po2properties ()
    elif len (sys.argv) == 2:
        po2properties (sys.argv[1])
    elif len (sys.argv) == 3:
        po2properties (sys.argv[1], sys.argv[2])
    else:
        usage (1)
    sys.exit (0)
