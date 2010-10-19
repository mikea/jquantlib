#!/usr/bin/env python

'''
# ===================================================================== 
#
# cpp2java is
#     Copyright (c) 2010, Richard Gomes
#
# All rights reserved.
#
# This source code is release under the GPL v3 License as published at
# http://www.opensource.org/licenses/gpl-3.0.html
#
# ===================================================================== 
'''






import os, sys
import re

from lxml import etree


'''
This class is responsible for parsing a XML file produced by Doxygen, containing
documentation obtained from C++ sources, and produce Java skeleton classes, which
contain field declarations and method declarations.

This class is configured to behave well translating QuantLib sources.
Ideally, typedefs should be passed to this class and not hardcoded as part of
this class.

@author: Richard Gomes <rgomes1997@yahoo.co.uk>
'''

class Grammar:
    __xpath_classes                   = '//compounddef[@kind="class" or @kind="struct"]'

    __xpath_methods_public            = '//memberdef[@kind="function" and @prot="public"    and @static="no"  and @virt != "pure-virtual" ]'
    __xpath_methods_protected         = '//memberdef[@kind="function" and @prot="protected" and @static="no"  and @virt != "pure-virtual" ]'
    __xpath_methods_private           = '//memberdef[@kind="function" and @prot="private"   and @static="no"  and @virt != "pure-virtual" ]'
    __xpath_methods_default           = '//memberdef[@kind="function" and @prot=""          and @static="no"  and @virt != "pure-virtual" ]'
    __xpath_methods_virtual_public    = '//memberdef[@kind="function" and @prot="public"    and @static="no"  and @virt  = "pure-virtual" ]'
    __xpath_methods_virtual_protected = '//memberdef[@kind="function" and @prot="protected" and @static="no"  and @virt  = "pure-virtual" ]'
    __xpath_methods_virtual_private   = '//memberdef[@kind="function" and @prot="private"   and @static="no"  and @virt  = "pure-virtual" ]'
    __xpath_methods_virtual_default   = '//memberdef[@kind="function" and @prot=""          and @static="no"  and @virt  = "pure-virtual" ]'
    __xpath_methods_static_public     = '//memberdef[@kind="function" and @prot="public"    and @static="yes" and @virt != "pure-virtual" ]'
    __xpath_methods_static_protected  = '//memberdef[@kind="function" and @prot="protected" and @static="yes" and @virt != "pure-virtual" ]'
    __xpath_methods_static_private    = '//memberdef[@kind="function" and @prot="private"   and @static="yes" and @virt != "pure-virtual" ]'
    __xpath_methods_static_default    = '//memberdef[@kind="function" and @prot=""          and @static="yes" and @virt != "pure-virtual" ]'

    __xpath_fields_public             = '//memberdef[@kind="variable" and @prot="public"    and @static="no"  and @mutable="no"  ]'
    __xpath_fields_protected          = '//memberdef[@kind="variable" and @prot="protected" and @static="no"  and @mutable="no"  ]'
    __xpath_fields_private            = '//memberdef[@kind="variable" and @prot="private"   and @static="no"  and @mutable="no"  ]'
    __xpath_fields_default            = '//memberdef[@kind="variable" and @prot=""          and @static="no"  and @mutable="no"  ]'
    __xpath_fields_mutable_public     = '//memberdef[@kind="variable" and @prot="public"    and @static="no"  and @mutable="yes" ]'
    __xpath_fields_mutable_protected  = '//memberdef[@kind="variable" and @prot="protected" and @static="no"  and @mutable="yes" ]'
    __xpath_fields_mutable_private    = '//memberdef[@kind="variable" and @prot="private"   and @static="no"  and @mutable="yes" ]'
    __xpath_fields_mutable_default    = '//memberdef[@kind="variable" and @prot=""          and @static="no"  and @mutable="yes" ]'
    __xpath_fields_static_public      = '//memberdef[@kind="variable" and @prot="public"    and @static="yes" and @mutable="no"  ]'
    __xpath_fields_static_protected   = '//memberdef[@kind="variable" and @prot="protected" and @static="yes" and @mutable="no"  ]'
    __xpath_fields_static_private     = '//memberdef[@kind="variable" and @prot="private"   and @static="yes" and @mutable="no"  ]'
    __xpath_fields_static_default     = '//memberdef[@kind="variable" and @prot=""          and @static="yes" and @mutable="no"  ]'


    def __init__(self, namespace, file):
        self.__namespace = namespace
        self.__file  = file

        self.__method_fixings = [ \
            [ re.compile('operator<<'),           'toString'  ],
            [ re.compile('operator\+='),          'addAssign' ],
            [ re.compile('operator-='),           'subAssign' ],
            [ re.compile('operator\*='),          'mulAssign' ],
            [ re.compile('operator/='),           'divAssign' ],
            [ re.compile('operator=='),           'eq'  ],
            [ re.compile('operator\!='),          'ne'  ],
            [ re.compile('operator>='),           'ge'  ],
            [ re.compile('operator<='),           'le'  ],
            [ re.compile('operator>'),            'gt'  ],
            [ re.compile('operator<'),            'lt'  ],
            [ re.compile('operator\+\+'),         'inc' ],
            [ re.compile('operator--'),           'dec' ],
            [ re.compile('operator\+'),           'add' ],
            [ re.compile('operator-'),            'sub' ],
            [ re.compile('operator\*'),           'mul' ],
            [ re.compile('operator/'),            'div' ],
            [ re.compile('operator='),            'assign' ],
            [ re.compile('operator\[\]'),         'at'  ],
            [ re.compile('operator\(\)'),         'get' ],
        ]


        self.__type_fixings = [ \
            [ re.compile('\*'),                   '' ],
            [ re.compile(' +'),                   '' ],

            [ re.compile('^const$'),              'final' ],
            [ re.compile('^const\s+'),            'final ' ],
            [ re.compile('^unsigned$'),           '/* @Unsigned */ int' ],

            [ re.compile('^bool$'),               'boolean' ],
            [ re.compile('^std::string$'),        'String' ],
            [ re.compile('^std::vector\s*<'),     'List<' ],
            [ re.compile('^std::map\s*<'),        'Map<' ],
            [ re.compile('^std::set\s*<'),        'Set<' ],

            [ re.compile('^boost::weak_ptr<'),    'WeakReference<' ],

##
##TODO: These are hardcoded definitions which should be passed to this class somehow
##
            [ re.compile('^Natural$'),            'int' ],
            [ re.compile('^BigInteger$'),         'int' ],
            [ re.compile('^Integer$'),            'int' ],
            [ re.compile('^Real$'),               'double' ],
            [ re.compile('^Size$'),               'int' ],
            [ re.compile('^Day$'),                'int' ],
            [ re.compile('^Year$'),               'int' ],

            [ re.compile('^CompoundFactor$'),     '/* @CompoundFactor */ double' ],
            [ re.compile('^Covariance$'),         '/* @Covariance */ double' ],
            [ re.compile('^Diffusion$'),          '/* @Diffusion */ double' ],
            [ re.compile('^DiscountFactor$'),     '/* @DiscountFactor */ double' ],
            [ re.compile('^Drift$'),              '/* @Drift */ double' ],
            [ re.compile('^Expectation$'),        '/* @Expectation */ double' ],
            [ re.compile('^Rate$'),               '/* @Rate */ double' ],
            [ re.compile('^Spread$'),             '/* @Spread */ double' ],
            [ re.compile('^StdDev$'),             '/* @StdDev */ double' ],
            [ re.compile('^Time$'),               '/* @Time */ double' ],
            [ re.compile('^Variance$'),           '/* @Variance */ double' ],
            [ re.compile('^Volatility$'),         '/* @Volatility */ double' ],

        ]


        self.__type_reference_fixings = [ \
            #[ re.compile('([^&]+)&'),             '/\\052 @NotNull \\052/ \\1' ],
        ]


        # We should be able to find the matching '>' and remove it too :~
        # Probably the easiest way to do it is:
        # 1. concatenate the entire type declaration
        # 2. find the string declared below and position the 'cursor' over the '<'
        # 3. find the matching '>'
        # 4. remove the matching '>'
        # 5. remove the string declared below
        self.__type_removal_fixings = [ \
            [ re.compile('boost::auto_ptr<'),     '' ],
            [ re.compile('boost::scoped_ptr<'),   '' ],
            [ re.compile('boost::scoped_array<'), '' ],
            [ re.compile('boost::shared_ptr<'),   '' ],
            [ re.compile('boost::shared_array<'), '' ],
            
            [ re.compile('::'),                   '.' ],
        ]


        self.__javadoc_fixings = [ \
            [ re.compile('<para\\s*[^>]*>'),                       '<p>' ],
            [ re.compile('</para>'),                               '</p>' ],

            [ re.compile('<p>\\s*<simplesect\\s+kind="note">'),    '@note ' ],
            [ re.compile('<p>\\s*<simplesect\\s+kind="warning">'), '@warning ' ],
            [ re.compile('<p>\\s*<simplesect\\s+kind="test">'),    '@test ' ],
            [ re.compile('<p>\\s*<simplesect\\s+kind="bug">'),     '@bug ' ],
            [ re.compile('</simplesect>\\s*</p>'),                 '' ],
            [ re.compile('</simplesect>'),                         '' ],
            
            [ re.compile('<p>\\s*<xrefsect'),                      '<xrefsect' ],
            [ re.compile('</xrefsect>\\s*</p>'),                   '</xrefsect>' ],
            [ re.compile('<xrefsect\\s*[^>]*>'),                   '' ],
            [ re.compile('</xrefsect>'),                           '' ],

            [ re.compile('<bold>'),                                '<b>' ],
            [ re.compile('</bold>'),                               '</b>' ],
            [ re.compile('<italic>'),                              '<i>' ],
            [ re.compile('</italic>'),                             '</i>' ],
            [ re.compile('<computeroutput>'),                      '<code>' ],
            [ re.compile('</computeroutput>'),                     '</code>' ],

            [ re.compile('<xreftitle>([^<]*)<'),                   '\\n@\\1 <' ],
            [ re.compile('</xreftitle>'),                          '' ],
            [ re.compile('<xrefdescription>'),                     '' ],
            [ re.compile('</xrefdescription>'),                    '' ],

            [ re.compile('<ref\\s+[^>]+>'),                        '{@link ' ],
            [ re.compile('</ref>'),                                '}' ],
            
            [ re.compile('<formula\\s+[^>]*>\\$'),                 '{@latex$' ],
            [ re.compile('\\$</formula>'),                         '}' ],
            [ re.compile('<formula\\s+[^>]*>\\\\\\['),             '{@latex[' ],
            [ re.compile('\\\\\\]</formula>'),                     '}' ],

            [ re.compile('std::string'),                           'String' ],
            [ re.compile('std::vector\\s*<'),                      'List<' ],
            [ re.compile('std::map\\s*<'),                         'Map<' ],
            [ re.compile('std::set\\s*<'),                         'Set<' ],
        ]



    def convert(self, folder):
        classes = etree.parse(self.__file).xpath(self.__xpath_classes)
        self.convert_classes(classes, folder)


    def convert_classes(self, classes, folder):
        for klass in classes:
            name = klass.xpath('./compoundname/text()')[0]
            kind = klass.get("kind")
            prot = klass.get("prot")
            comments = self.javadoc('', klass)
            
            # remove namespace
            p = re.compile(self.__namespace+'::');
            name = p.sub('', name);
            
            #create output file
            self.__output = open(os.path.join(folder, name+'.java'), 'w')
            if len(comments) > 0:
                self.__output.write(comments)
            self.__output.write( \
                prot + ' ' + kind + ' ' + name + \
                self.convertBaseClasses(klass) + \
                self.convertBaseInterfaces(klass) + ' {\n' )
            self.convertAllFields(klass)
            self.convertAllMethods(klass)
            self.__output.write('}\n')


    def convertBaseClasses(self, klass):
        classes = klass.xpath('//basecompoundref[@virt="non-virtual"]')
        return self.elements(' extends ', classes, '')


    def convertBaseInterfaces(self, klass):
        interfaces = klass.xpath('//basecompoundref[@virt="virtual"]')
        return self.elements(' implements ', interfaces, '')


    def convertAllMethods(self, klass):
          methods_public            = klass.xpath(self.__xpath_methods_public)
          methods_protected         = klass.xpath(self.__xpath_methods_protected)
          methods_private           = klass.xpath(self.__xpath_methods_private)
          methods_default           = klass.xpath(self.__xpath_methods_default)
          methods_virtual_public    = klass.xpath(self.__xpath_methods_virtual_public)
          methods_virtual_protected = klass.xpath(self.__xpath_methods_virtual_protected)
          methods_virtual_private   = klass.xpath(self.__xpath_methods_virtual_private)
          methods_virtual_default   = klass.xpath(self.__xpath_methods_virtual_default)
          methods_static_public     = klass.xpath(self.__xpath_methods_static_public)
          methods_static_protected  = klass.xpath(self.__xpath_methods_static_protected)
          methods_static_private    = klass.xpath(self.__xpath_methods_static_private)
          methods_static_default    = klass.xpath(self.__xpath_methods_static_default)
          self.convertMethods(methods_public,            'public methods')
          self.convertMethods(methods_protected,         'protected methods')
          self.convertMethods(methods_private,           'private methods')
          self.convertMethods(methods_default,           '@PackagePrivate methods')
          self.convertMethods(methods_virtual_public,    'public abstract methods')
          self.convertMethods(methods_virtual_protected, 'protected abstract methods')
          self.convertMethods(methods_virtual_private,   'private abstract methods')
          self.convertMethods(methods_virtual_default,   '@PackagePrivate abstract methods')
          self.convertMethods(methods_static_public,     'public static methods')
          self.convertMethods(methods_static_protected,  'protected static methods')
          self.convertMethods(methods_static_private,    'private static methods')
          self.convertMethods(methods_static_default,    '@PackagePrivate static methods')


    def convertMethods(self, elements, header):
        if len(elements) == 0:
            return
        self.__output.write('\n')
        self.__output.write('\n')
        self.__output.write('    //\n')
        self.__output.write('    // ' + header + '\n')
        self.__output.write('    //\n')
        self.__output.write('\n')
        for elem in elements:
            name = elem.xpath('./name/text()')[0]
            hasOverride = len(elem.xpath('./reimplements'))>0
            static      = ' static'        if elem.get("static") == "yes"          else ''
            abstract    = ' abstract'      if elem.get("virt")   == "pure-virtual" else ''
            const       = ' /*@ReadOnly*/' if elem.get("const")  == "yes"          else ''
            visibility  = elem.get("prot")
            comments = self.javadoc('    ', elem)

            name = self.applyFixings(name, self.__method_fixings)

            if len(comments) > 0:
                self.__output.write(comments)

            if hasOverride:
                self.__output.write('    @Override\n')
                
            type = self.convertType(elem)
            if len(type)>0:
                type = ' ' + type
            self.__output.write( \
                '    ' + visibility + static + abstract + type + ' ' + name +
                '(' + self.convertParams(elem) + ')' + const )
            if abstract == '':
                self.__output.write( \
                    ' {\n' +
                    '        throw new UnsupportedOperationException();\n' + 
                    '    }\n\n' )
            else:
                self.__output.write( ' ;\n\n' )


    def convertParams(self, method):
        elements = method.xpath('./param')
        nelem = 0
        str = ''
        for elem in self.paramIterator(elements):
            text = self.applyFixings(elem, self.__type_reference_fixings)
            text = self.applyFixings(text, self.__type_fixings)
            text = self.applyFixings(text, self.__type_removal_fixings)
            str += elem if nelem==0 else ', ' + elem
            nelem += 1
        return str


    def paramIterator(self, elements):
        if len(elements)==0:
            return
        for elem in elements:
            hasDeclName = len(elem.xpath('./declname'))>0
            if hasDeclName:
                declName = elem.xpath('./declname/text()')[0]
            hasDefName = len(elem.xpath('./defname'))>0
            if hasDefName:
                defName = elem.xpath('./defname/text()')[0]
            hasName = len(elem.xpath('./name'))>0
            if hasName:
                name = elem.xpath('./name/text()')[0]
            text = declName if hasDeclName else defName if hasDefName else name if hasName else 'ref'
            str = self.convertType(elem) + ' ' + text
            yield str # return element to caller
        return # no more elements generated


    def convertAllFields(self, klass):
          fields_public            = klass.xpath(self.__xpath_fields_public)
          fields_protected         = klass.xpath(self.__xpath_fields_protected)
          fields_private           = klass.xpath(self.__xpath_fields_private)
          fields_default           = klass.xpath(self.__xpath_fields_default)
          fields_mutable_public    = klass.xpath(self.__xpath_fields_mutable_public)
          fields_mutable_protected = klass.xpath(self.__xpath_fields_mutable_protected)
          fields_mutable_private   = klass.xpath(self.__xpath_fields_mutable_private)
          fields_mutable_default   = klass.xpath(self.__xpath_fields_mutable_default)
          fields_static_public     = klass.xpath(self.__xpath_fields_static_public)
          fields_static_protected  = klass.xpath(self.__xpath_fields_static_protected)
          fields_static_private    = klass.xpath(self.__xpath_fields_static_private)
          fields_static_default    = klass.xpath(self.__xpath_fields_static_default)
          self.convertFields(fields_public,            'public fields')
          self.convertFields(fields_protected,         'protected fields')
          self.convertFields(fields_private,           'private fields')
          self.convertFields(fields_default,           '@PackagePrivate fields')
          self.convertFields(fields_mutable_public,    'public MUTABLE fields //TODO: code review : mutable field')
          self.convertFields(fields_mutable_protected, 'protected MUTABLE fields //TODO: code review : mutable field')
          self.convertFields(fields_mutable_private,   'private MUTABLE fields //TODO: code review : mutable field')
          self.convertFields(fields_mutable_default,   '@PackagePrivate MUTABLE fields //TODO: code review : mutable field')
          self.convertFields(fields_static_public,     'public static fields')
          self.convertFields(fields_static_protected,  'protected static fields')
          self.convertFields(fields_static_private,    'private static fields')
          self.convertFields(fields_static_default,    '@PackagePrivate static fields')


    def convertFields(self, elements, header):
        if len(elements) == 0:
            return
        self.__output.write('\n')
        self.__output.write('\n')
        self.__output.write('    //\n')
        self.__output.write('    // ' + header + '\n')
        self.__output.write('    //\n')
        self.__output.write('\n')
        for elem in elements:
            name = elem.xpath('./name/text()')[0]
            hasOverride = len(elem.xpath('./reimplements'))>0
            static      = ' static'               if elem.get("static") == "yes"  else ''
            mutable     = ' /* @Mutable */'       if elem.get("mutable") == "yes" else ''
            ##-- const       = ' /*@ReadOnly*/' if elem.get("const") == "yes"   else ''
            visibility  = elem.get("prot")
            comments = self.javadoc('    ', elem)
            
            if len(comments) > 0:
                self.__output.write(comments)

            self.__output.write( \
                '    ' + visibility + static + mutable + ' ' +
                self.convertType(elem) + ' ' + name + ';\n')


    def convertType(self, node):
        nelem = 0
        str = ''
        for elem in self.typeIterator(node):
            str += elem if nelem==0 else ' ' + elem
            nelem += 1
        return '' if nelem==0 else str


    def typeIterator(self, node):
        elements = node.xpath('./type/node()')
        if len(elements)==0:
            return # no more elements generated
        for elem in elements:
            if etree.iselement(elem):
                #kind = elem.xpath('./@kindref')[0]
                #print kind
                #if kind == "compound" :
                #    yield "/*@NotNull*/"
                ref = elem.xpath('./text()')
                for relem in ref:
                    text = self.applyFixings(relem, self.__type_reference_fixings)
                    text = self.applyFixings(text,  self.__type_fixings)
                    text = self.applyFixings(text,  self.__type_removal_fixings)
                    yield text
            else:
                text = self.applyFixings(elem, self.__type_reference_fixings)
                text = self.applyFixings(text, self.__type_fixings)
                text = self.applyFixings(text, self.__type_removal_fixings)
                yield text
        return # no more elements generated


    def applyFixings(self, text, fixings):
        for fixing in fixings:
            text = fixing[0].sub(fixing[1], text)
        return text


    def elements(self, prefix, elements, suffix):
        nelem = 0
        str = ''
        for elem in self.iterator(elements):
            str += elem if nelem==0 else ', ' + elem
            nelem += 1
        return '' if nelem==0 else prefix + str + suffix


    def iterator(self, elements):
        if len(elements)==0:
            return
        for elem in elements:
            name = elem.xpath('./text()')[0]
            # remove namespace
            p = re.compile(self.__namespace+'::');
            text = p.sub('', name);
            yield text # return element to caller
        return # no more elements generated


    def javadoc(self, indent, node):
        bdesc = node.xpath('./briefdescription/*')
        ddesc = node.xpath('./detaileddescription/*')
        if (bdesc == None or len(bdesc) == 0) and (ddesc == None or len(ddesc) == 0):
            return ''
        text = indent + '/**' + '\n'
        for elem in bdesc:
            #print etree.tostring(elem)
            text += indent + ' * ' + etree.tostring(elem) + '\n'
        for elem in ddesc:
            #print etree.tostring(elem)
            text += indent + ' * ' + etree.tostring(elem) + '\n'
        text += indent + ' */' + '\n'
        return self.applyFixings(text, self.__javadoc_fixings)


    def javadocIterator(self, node):
        elements = node.xpath('./node()')
        if len(elements)==0:
            return # no more elements generated
        for elem in elements:
            if etree.iselement(elem):
                ref = elem.xpath('./text()')
                for relem in ref:
                    text = self.applyFixings(relem, self.__javadoc_fixings)
                    yield text
            else:
                text = self.applyFixings(elem, self.__javadoc_fixings)
                yield text
        return # no more elements generated






##
## Main program
##
## Usage: cpp2java -v -O outputdir --namespace QuantLib xmldir
##

from optparse import OptionParser



def main():
    parser = OptionParser(usage="%prog -h -O output-folder input-folders...")
    ## see: http://www.doughellmann.com/PyMOTW/optparse/
    parser.add_option('-v', '--verbose',
                      action='store_true',
                      dest='verbose',
                      help='Verbose: Lists files being processed')
    parser.add_option('-n', '--namespace',
                      action='store',
                      dest='namespace',
                      help='C++ namespace to removed')
    parser.add_option('-O', '--output',
                      action='store',
                      dest='output',
                      metavar='folder',
                      default='.',
                      help='Optional output folder where Java skeletons will be produced. Defaults to the current folder')

    (opts, args) = parser.parse_args()

    if not opts.namespace:
        parser.print_help()
        sys.exit(1)

    for folder in args:
        for dirname, dirnames, filenames in os.walk(folder):
            for filename in sorted(filenames):
                if filename[-4:]==".xml":
                    file = os.path.join(dirname, filename)
                    grammar = Grammar(opts.namespace, file)
                    if opts.verbose:
                        print file
                    try:
                        grammar.convert(opts.output)
                    except Exception, e:
                        print e


#
# Main program activator
#
if __name__ == "__main__":
    main()
    sys.exit(0)
