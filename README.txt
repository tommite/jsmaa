README for JSMAA
================
JSMAA implements Stochastic Multicriteria Acceptability Analysis (SMAA) MCDA methodology. Currently is implemented:
-SMAA-2 for multi-attribute value/utility-theory based decision analysis (ranking / choosing problem statement).
-SMAA-TRI for outranking based sorting problems.
-SMAA-O for ordinal criteria measurements (part of SMAA-2-model).

Versions
========
1.0.3:	Fix results screens not working on OS X Mavericks.
1.0.2:	Fix incorrect pessimistic assignment rule implementation in SMAA-TRI.
1.0.1:	Big fixes.
1.0:	Discrete measurements, piecewise linear partial value functions.
0.8.6:	Bug fixes.
0.8.5:	Bug fixes.
0.8.4:	Fix sampling of baselines for relative measurements.
0.8.3:	LogitNormal, RelativeNormal, and RelativeLogNormal measurements.
0.8.2:	Fixes: icons to work on Windows, negative interval calculation, slowness in simulations, chart style to 2d.
	Added tooltip for rank acceptability- and CW table headers, changed to not construct any charts for 20+ alternatives.
0.8.1:	Better beta-sampler, value function graphs, refactoring.
0.8:    Beta-distributed measurements, GUI improvements, heavy refactoring.
0.6.4:	Changed to not showing acceptabilities graphs for 20+ alternatives. Added measurements to view of
	an alternative. Minor fixes & refactors for better integrability.
0.6.3:	Added showing of scales to preference information view. Major refactors and small fixes.
0.6.2:	Add exporting of GNUPlot scripts for figures. Fix SMAA-2 simulation crash on adding criterion.
0.6.1:	Fix major bug in previous versions of not updating output graphs/tables when they are showing.
0.6:	XML model files, ordinal criteria, improved output tables, reordering of alternatives/criteria/categories,
	improved tooltips, and various minor improvements.
0.4.1:	Improvements of the GUI usability and bug fixes. 
0.4:	Adds SMAA-TRI, uncertainty individually for measurements, cardinal preference information, graph output,
	and various minor improvements.
0.2:	Initial release implementing SMAA-2. 

License
=======
JSMAA is (c) 2009-2010 Tommi Tervonen, (c) 2011 Tommi Tervonen and Gert van Valkenhoef,
(c) 2012 Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper and Daan Reid, (c) 2013-2015
Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas and
licensed under GPL(3). See LICENSE.txt for more details. Icons are taken from Eclipse, 
and are (c) Eclipse foundations and of their corresponding authors.

Contact
=======
Contact me by email at tommi at smaa dot fi.

More information can be found at http://www.smaa.fi.

January 2015
Tommi Tervonen
