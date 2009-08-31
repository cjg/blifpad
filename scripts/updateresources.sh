#!/bin/bash

# updateresurces.sh
 
#   
# BlueLan -- <project_description>
#  
# Copyright (C) 2006 - 2007
#     Giuseppe Coviello <cjg@cruxppc.org>
#  
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#  
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#  
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

LANGS=it
for l in $LANGS; do
    ./scripts/properties2po.py "properties/MyResources_${l}.properties" "${l}.po"
    UPDATE=""
    if [ -e "${l}.po" ]; then
	echo exis
	UPDATE=-j
    fi
    xgettext -o "${l}.po" --language=Java $UPDATE --keyword="_" src/*.java
    ./scripts/po2properties.py "${l}.po" "properties/MyResources_${l}.properties"
    rm "${l}.po"
done

./scripts/properties2po.py "properties/MyResources.properties" "messages.po"
UPDATE=""
if [ -e "${l}.po" ]; then
    UPDATE=-j
fi
xgettext -o "messages.po" --language=Java $UPDATE --keyword="_" src/*.java
./scripts/po2properties.py "messages.po" "properties/MyResources.properties"
rm "messages.po"

# End Of File
