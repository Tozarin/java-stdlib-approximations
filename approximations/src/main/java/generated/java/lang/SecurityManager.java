// Generated by the LibSL translator.  DO NOT EDIT!
// sources:
//  - java/lang/SecurityManager.lsl:31
//  - java/lang/SecurityManager.main.lsl:24
//
package generated.java.lang;

import java.io.FileDescriptor;
import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.SecurityException;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.lang.Thread;
import java.lang.ThreadGroup;
import java.lang.Void;
import java.net.InetAddress;
import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.Permission;
import org.jacodb.approximation.annotation.Approximate;
import org.usvm.api.Engine;
import runtime.LibSLRuntime;

/**
 * SecurityManagerAutomaton for LSLSecurityManager ~> java.lang.SecurityManager
 */
@SuppressWarnings({"all", "unchecked"})
@Approximate(java.lang.SecurityManager.class)
public class SecurityManager implements LibSLRuntime.Automaton {
    static {
        Engine.assume(true);
    }

    private byte __$lsl_state = __$lsl_States.Allocated;

    @LibSLRuntime.AutomatonConstructor
    public SecurityManager(Void __$lsl_token, final byte p0) {
        this.__$lsl_state = p0;
    }

    @LibSLRuntime.AutomatonConstructor
    public SecurityManager(final Void __$lsl_token) {
        this(__$lsl_token, __$lsl_States.Allocated);
    }

    /**
     * [CONSTRUCTOR] SecurityManagerAutomaton::<init>(LSLSecurityManager) -> void
     * Source: java/lang/SecurityManager.main.lsl:100
     */
    public SecurityManager() {
        this((Void) null);
        Engine.assume(this.__$lsl_state == __$lsl_States.Allocated);
        /* body */ {
            _do_checkPermission(new RuntimePermission("createSecurityManager"));
        }
        this.__$lsl_state = __$lsl_States.Initialized;
    }

    /**
     * [SUBROUTINE] SecurityManagerAutomaton::_do_checkPermission(Permission) -> void
     * Source: java/lang/SecurityManager.main.lsl:88
     */
    private void _do_checkPermission(Permission perm) {
        /* body */ {
            if (Engine.makeSymbolicBoolean()) {
                throw new AccessControlException("access denied ", perm);
            }
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkAccept(LSLSecurityManager, String, int) -> void
     * Source: java/lang/SecurityManager.main.lsl:112
     */
    public void checkAccept(String host, int port) {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            if (host == null) {
                throw new NullPointerException();
            }
            if (Engine.makeSymbolicBoolean()) {
                throw new IllegalArgumentException();
            }
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkAccess(LSLSecurityManager, Thread) -> void
     * Source: java/lang/SecurityManager.main.lsl:127
     */
    public void checkAccess(Thread t) {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            if (t == null) {
                throw new NullPointerException("thread can't be null");
            }
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkAccess(LSLSecurityManager, ThreadGroup) -> void
     * Source: java/lang/SecurityManager.main.lsl:140
     */
    public void checkAccess(ThreadGroup g) {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            if (g == null) {
                throw new NullPointerException("thread group can't be null");
            }
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkConnect(LSLSecurityManager, String, int) -> void
     * Source: java/lang/SecurityManager.main.lsl:153
     */
    public void checkConnect(String host, int port) {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            if (host == null) {
                throw new NullPointerException("host can't be null");
            }
            if (Engine.makeSymbolicBoolean()) {
                throw new IllegalArgumentException();
            }
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkConnect(LSLSecurityManager, String, int, Object) -> void
     * Source: java/lang/SecurityManager.main.lsl:168
     */
    public void checkConnect(String host, int port, Object context) {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            if (host == null) {
                throw new NullPointerException("host can't be null");
            }
            if (Engine.makeSymbolicBoolean()) {
                throw new IllegalArgumentException();
            }
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkCreateClassLoader(LSLSecurityManager) -> void
     * Source: java/lang/SecurityManager.main.lsl:183
     */
    public void checkCreateClassLoader() {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkDelete(LSLSecurityManager, String) -> void
     * Source: java/lang/SecurityManager.main.lsl:191
     */
    public void checkDelete(String file) {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            if (file == null) {
                throw new NullPointerException();
            }
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkExec(LSLSecurityManager, String) -> void
     * Source: java/lang/SecurityManager.main.lsl:204
     */
    public void checkExec(String cmd) {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            if (cmd == null) {
                throw new NullPointerException();
            }
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkExit(LSLSecurityManager, int) -> void
     * Source: java/lang/SecurityManager.main.lsl:217
     */
    public void checkExit(int status) {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkLink(LSLSecurityManager, String) -> void
     * Source: java/lang/SecurityManager.main.lsl:225
     */
    public void checkLink(String lib) {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            if (lib == null) {
                throw new NullPointerException();
            }
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkListen(LSLSecurityManager, int) -> void
     * Source: java/lang/SecurityManager.main.lsl:236
     */
    public void checkListen(int port) {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkMulticast(LSLSecurityManager, InetAddress) -> void
     * Source: java/lang/SecurityManager.main.lsl:244
     */
    public void checkMulticast(InetAddress maddr) {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            if (maddr == null) {
                throw new NullPointerException();
            }
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkMulticast(LSLSecurityManager, InetAddress, byte) -> void
     * Source: java/lang/SecurityManager.main.lsl:255
     */
    public void checkMulticast(InetAddress maddr, byte ttl) {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            if (maddr == null) {
                throw new NullPointerException();
            }
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkPackageAccess(LSLSecurityManager, String) -> void
     * Source: java/lang/SecurityManager.main.lsl:266
     */
    public void checkPackageAccess(String pkg) {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            if (pkg == null) {
                throw new NullPointerException();
            }
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkPackageDefinition(LSLSecurityManager, String) -> void
     * Source: java/lang/SecurityManager.main.lsl:277
     */
    public void checkPackageDefinition(String pkg) {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            if (pkg == null) {
                throw new NullPointerException();
            }
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkPermission(LSLSecurityManager, Permission) -> void
     * Source: java/lang/SecurityManager.main.lsl:288
     */
    public void checkPermission(Permission perm) {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            if (perm == null) {
                throw new NullPointerException();
            }
            _do_checkPermission(perm);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkPermission(LSLSecurityManager, Permission, Object) -> void
     * Source: java/lang/SecurityManager.main.lsl:297
     */
    public void checkPermission(Permission perm, Object context) {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            if ((context instanceof AccessControlContext)) {
                if (perm == null) {
                    throw new NullPointerException();
                }
                _do_checkPermission(perm);
            } else {
                throw new SecurityException();
            }
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkPrintJobAccess(LSLSecurityManager) -> void
     * Source: java/lang/SecurityManager.main.lsl:313
     */
    public void checkPrintJobAccess() {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkPropertiesAccess(LSLSecurityManager) -> void
     * Source: java/lang/SecurityManager.main.lsl:321
     */
    public void checkPropertiesAccess() {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkPropertyAccess(LSLSecurityManager, String) -> void
     * Source: java/lang/SecurityManager.main.lsl:329
     */
    public void checkPropertyAccess(String key) {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkRead(LSLSecurityManager, FileDescriptor) -> void
     * Source: java/lang/SecurityManager.main.lsl:337
     */
    public void checkRead(FileDescriptor fd) {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkRead(LSLSecurityManager, String) -> void
     * Source: java/lang/SecurityManager.main.lsl:345
     */
    public void checkRead(String file) {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkRead(LSLSecurityManager, String, Object) -> void
     * Source: java/lang/SecurityManager.main.lsl:353
     */
    public void checkRead(String file, Object context) {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkSecurityAccess(LSLSecurityManager, String) -> void
     * Source: java/lang/SecurityManager.main.lsl:361
     */
    public void checkSecurityAccess(String _target) {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            if (_target == null) {
                throw new NullPointerException();
            }
            if (_target.isEmpty()) {
                throw new IllegalArgumentException();
            }
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkSetFactory(LSLSecurityManager) -> void
     * Source: java/lang/SecurityManager.main.lsl:375
     */
    public void checkSetFactory() {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkWrite(LSLSecurityManager, FileDescriptor) -> void
     * Source: java/lang/SecurityManager.main.lsl:383
     */
    public void checkWrite(FileDescriptor fd) {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::checkWrite(LSLSecurityManager, String) -> void
     * Source: java/lang/SecurityManager.main.lsl:391
     */
    public void checkWrite(String file) {
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            _do_checkPermission(null);
        }
    }

    /**
     * [FUNCTION] SecurityManagerAutomaton::getSecurityContext(LSLSecurityManager) -> Object
     * Source: java/lang/SecurityManager.main.lsl:399
     */
    public Object getSecurityContext() {
        Object result = null;
        Engine.assume(this.__$lsl_state == __$lsl_States.Initialized);
        /* body */ {
            result = Engine.makeSymbolic(AccessControlContext.class);
            Engine.assume(result != null);
        }
        return result;
    }

    public static final class __$lsl_States {
        public static final byte Allocated = (byte) 0;

        public static final byte Initialized = (byte) 1;
    }

    @Approximate(SecurityManager.class)
    public static final class __hook {
        private __hook(Void o1, Void o2) {
            Engine.assume(false);
        }
    }
}
