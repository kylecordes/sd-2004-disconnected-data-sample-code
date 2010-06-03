/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

Released 2004-04-26

kyle@kylecordes



I used Eclipse for this, and included the .project and .classpath
files needed.  Normally I'd also support an Ant build file, I'd
be happy to add one if someone wants to create it.

----------------------------

List of libraries used; most are all present here in this archive:

----------------------------

To run the JMS-based mode, you will need to have an UberMQ
server running on your machine:

http://www.ubermq.com/

Also put ubermq-client.jar in the lib directory.  I did not have time
to consider what restrictions there might be on including it here
(it is under a GPL license) so I left it out.

----------------------------

org.apache.commons.jrcs.*  is the JRCS package, which is
in the process of being moved in to the Apache family:

http://jakarta.apache.org/commons/sandbox/jrcs/

I put the source in here instead of the Jar file, so that you
can easily look at the code there to create and apply "diffs".

----------------------------

Hessian is an RMI-like facility for making remote calls over
HTTP.  I used it here just for a reason to learn about it.
Unlike RMI, it has not built-time process (rmic), it works
at runtime with dynamic proxies.

http://www.caucho.com/hessian/

----------------------------

Jetty is an open source, embeddable HTTP server.  It is
here in the form of one Jar and needs no config file, it can be
configured in Java code (as it is in this example code)

http://jetty.mortbay.org/

-----------------------------

Jakarta-ORO is needed by JRCS, though this example does not
use the features of JRCS that invoke it.

-----------------------------

Enjoy!

-Kyle Cordes

