/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/*
 *
 *  Portions, Copyright © 1991-2005 Unicode, Inc. The following applies to Unicode. 
 *
 *  COPYRIGHT AND PERMISSION NOTICE
 *
 *  Copyright © 1991-2005 Unicode, Inc. All rights reserved. Distributed under 
 *  the Terms of Use in http://www.unicode.org/copyright.html. Permission is
 *  hereby granted, free of charge, to any person obtaining a copy of the
 *  Unicode data files and any associated documentation (the "Data Files")
 *  or Unicode software and any associated documentation (the "Software") 
 *  to deal in the Data Files or Software without restriction, including without
 *  limitation the rights to use, copy, modify, merge, publish, distribute,
 *  and/or sell copies of the Data Files or Software, and to permit persons
 *  to whom the Data Files or Software are furnished to do so, provided that 
 *  (a) the above copyright notice(s) and this permission notice appear with
 *  all copies of the Data Files or Software, (b) both the above copyright
 *  notice(s) and this permission notice appear in associated documentation,
 *  and (c) there is clear notice in each modified Data File or in the Software
 *  as well as in the documentation associated with the Data File(s) or Software
 *  that the data or software has been modified.

 *  THE DATA FILES AND SOFTWARE ARE PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 *  KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT 
 *  OF THIRD PARTY RIGHTS. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR HOLDERS
 *  INCLUDED IN THIS NOTICE BE LIABLE FOR ANY CLAIM, OR ANY SPECIAL INDIRECT
 *  OR CONSEQUENTIAL DAMAGES, OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS
 *  OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR
 *  OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 *  PERFORMANCE OF THE DATA FILES OR SOFTWARE.
 *
 *  Except as contained in this notice, the name of a copyright holder shall
 *  not be used in advertising or otherwise to promote the sale, use or other
 *  dealings in these Data Files or Software without prior written
 *  authorization of the copyright holder.
 *
 *  2. Additional terms from the Database:
 *
 *  Copyright © 1995-1999 Unicode, Inc. All Rights reserved.
 *
 *  Disclaimer 
 *
 *  The Unicode Character Database is provided as is by Unicode, Inc.
 *  No claims are made as to fitness for any particular purpose. No warranties
 *  of any kind are expressed or implied. The recipient agrees to determine
 *  applicability of information provided. If this file has been purchased
 *  on magnetic or optical media from Unicode, Inc., the sole remedy for any claim
 *  will be exchange of defective media within 90 days of receipt. This disclaimer
 *  is applicable for all other data files accompanying the Unicode Character Database,
 *  some of which have been compiled by the Unicode Consortium, and some of which
 *  have been supplied by other sources.
 *
 *  Limitations on Rights to Redistribute This Data
 *
 *  Recipient is granted the right to make copies in any form for internal
 *  distribution and to freely use the information supplied in the creation of
 *  products supporting the UnicodeTM Standard. The files in 
 *  the Unicode Character Database can be redistributed to third parties or other
 *  organizations (whether for profit or not) as long as this notice and the disclaimer
 *  notice are retained. Information can be extracted from these files and used
 *  in documentation or programs, as long as there is an accompanying notice
 *  indicating the source. 
 */

package kotlin.text.regex

/*
 * This class is a range that contains only surrogate characters.
 *
 * @author Nikolay A. Kuznetsov
 */
internal class SurrogateRangeSet(surrChars: AbstractCharClass) : RangeSet(surrChars) {

    override fun accepts(startIndex: Int, testString: CharSequence): Int {
        val result = super.accepts(startIndex, testString)
        when {
            result < 0 ||
            testString.isHighSurrogate(startIndex - 1) && testString.isLowSurrogate(startIndex) ||
            testString.isHighSurrogate(startIndex) && testString.isLowSurrogate(startIndex + 1) ->
                return -1
        }
        return result
    }

    private fun CharSequence.isHighSurrogate(index: Int, leftBound: Int = 0, rightBound: Int = length)
            = (index in leftBound until rightBound && this[index].isHighSurrogate())

    private fun CharSequence.isLowSurrogate(index: Int, leftBound: Int = 0, rightBound: Int = length)
            = (index in leftBound until rightBound && this[index].isLowSurrogate())

    override fun first(set: AbstractSet): Boolean {
        return when (set) {
            is SurrogateRangeSet -> true // TODO: May be add some check here.
            is CharSet,
            is RangeSet,
            is SupplementaryCharSet,
            is SupplementaryRangeSet -> false
            else -> true
        }
    }

    override fun hasConsumed(matchResult: MatchResultImpl): Boolean = true
}