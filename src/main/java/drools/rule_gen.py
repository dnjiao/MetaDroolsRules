#!/usr/bin/env python

import os, sys

num = sys.argv[1]

filename = 'rule' + num + '.drl'

outfile = open(filename, 'w')

outfile.write('package drools.rules\n\n')
outfile.write('import drools.DicomImage\n\n')

for i in range(int(num)):
    outfile.write('rule \"PHI rule' + str(i) + '\"\n\n')
    outfile.write('    when\n')
    outfile.write('        $di : DicomImage(boolName == true)\n')
    outfile.write('    then\n')
    outfile.write('        System.out.println(\"rule ' + str(i) + ' is applied\");\n')
    outfile.write('end\n\n')
