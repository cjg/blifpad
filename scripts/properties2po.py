#!/usr/bin/python
"""
    properties2po.py
    
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

def unescape (text):
    
    return text.replace ("\ ", " ").replace ("\:", ":")

def properties2po (filein=None, fileout=None):
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
    for line in lines:
        if line[0] == "\"":
            fdout.write (line)
            continue
        if "=" in line:
            if line[0] == "!":
                line = line[1:]
            msgid, msgstr = line[:-1].split ("=", 2)
            msgid = unescape (msgid)
            fdout.write ("msgid \"%s\"\nmsgstr \"%s\"\n" % (msgid, msgstr))
            continue
        fdout.write (line)
    fdout.close ()

def usage (exit_code=1):
    print "Usage: ./properties2po.py [INPUTFILE] [OUTPUTFILE]"
    print "Converts Java properties file into po gettext format."
    print "-h, --help    This help screen"
    print
    print "Report bugs to <cjg@cruxppc.org>"
    sys.exit (exit_code)
    
if __name__ == "__main__":
    if "-h" in sys.argv or "--help" in sys.argv:
        usage (0)
    if len (sys.argv) == 1:
        properties2po ()
    elif len (sys.argv) == 2:
        properties2po (sys.argv[1])
    elif len (sys.argv) == 3:
        properties2po (sys.argv[1], sys.argv[2])
    else:
        usage (1)
    sys.exit (0)
