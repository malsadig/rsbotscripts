package org.sol.script.grenwall.util;

import org.powerbot.script.Tile;
import org.powerbot.script.rt6.*;
import org.sol.api.path.Obstacle;
import org.sol.core.script.Contextable;
import org.sol.core.script.Methods;
import org.sol.util.Area;
import org.sol.util.Condition;

public class TeleportMethod extends Contextable {

    private Area bankArea;
    private TilePath pathToBank;
    private Method method;

    public TeleportMethod(Methods methods) {
        super(methods);
    }

    public void init(final Method method) {
        this.method = method;
        this.pathToBank = method.getPathToBank(methods);
        this.bankArea = method.bankArea;
    }

    public TilePath getPathToBank() {
        return this.pathToBank;
    }

    public Area getBankArea() {
        return this.bankArea;
    }

    public Method getMethod() {
        return this.method;
    }

    public enum Method {
        VARROCK(new Area(new Tile(3101, 3519, 0), new Tile(3291, 3347, 0))) {
            @Override
            public TilePath getPathToBank(Methods methods) {
                return new TilePath(methods.context, new Tile[]{new Tile(3163, 3461, 0), new Tile(3163, 3466, 0),
                        new Tile(3164, 3471, 0), new Tile(3169, 3473, 0), new Tile(3174, 3474, 0)});
            }

            @Override
            public void teleport(final Methods methods) {
                if (methods.context.hud.floating(Hud.Window.BACKPACK) || methods.context.hud.open(Hud.Window.BACKPACK)) {
                    for (final Item teletab : methods.context.backpack.select().id(Invariants.VARROCK_TELETAB).first()) {
                        final Component widget;
                        if ((widget = teletab.component()) != null) {
                            if (widget.interact("Break")) {
                                methods.sleep.sleep(new Condition() {
                                    @Override
                                    public boolean accept() {
                                        return true;
                                    }
                                }, 7000);
                            }
                        }
                    }
                }
            }

            @Override
            public Obstacle[] getReturnPath(final Methods methods) {
                return new Obstacle[]{
                        new Obstacle(methods, "Protruding Rocks", 3927, "Climb") {
                            @Override
                            public boolean accept() {
                                return !methods.context.objects.select(10).id(this.getId()).first().isEmpty();
                            }
                        },
                        new Obstacle(methods, "Spirit Tree 1", Invariants.VARROCK_SPIRIT_TREE, "Teleport",
                                new Tile(3187, 3444, 0), new Tile(3187, 3449, 0), new Tile(3182, 3451, 0),
                                new Tile(3177, 3451, 0), new Tile(3173, 3454, 0), new Tile(3168, 3457, 0),
                                new Tile(3167, 3462, 0), new Tile(3166, 3467, 0), new Tile(3167, 3472, 0),
                                new Tile(3170, 3476, 0), new Tile(3171, 3481, 0), new Tile(3172, 3486, 0),
                                new Tile(3175, 3490, 0), new Tile(3177, 3495, 0), new Tile(3180, 3499, 0),
                                new Tile(3183, 3503, 0), new Tile(3187, 3507, 0)) {
                            @Override
                            public boolean accept() {
                                return !methods.context.objects.select(10).id(this.getId()).first().isEmpty();
                            }
                        },
                        new Obstacle(methods, "Spirit Tree 2", Invariants.GNOME_SPIRIT_TREE, "Teleport") {
                            @Override
                            public boolean accept() {
                                return !methods.context.objects.select(10).id(this.getId()).first().isEmpty();
                            }
                        },
                        new Obstacle(methods, "Stronghold Gate", 68983, "Open",
                                new Tile(2464, 3447, 0), new Tile(2464, 3442, 0), new Tile(2463, 3437, 0),
                                new Tile(2462, 3432, 0), new Tile(2461, 3427, 0), new Tile(2459, 3422, 0),
                                new Tile(2458, 3417, 0), new Tile(2458, 3412, 0), new Tile(2459, 3407, 0),
                                new Tile(2460, 3402, 0), new Tile(2460, 3397, 0), new Tile(2460, 3392, 0),
                                new Tile(2461, 3387, 0), new Tile(2461, 3382, 0)) {
                            @Override
                            public boolean accept() {
                                return methods.context.players.local().tile().y() >= 3385
                                        && methods.context.skills.level(Constants.SKILLS_SUMMONING)
                                        >= methods.context.skills.realLevel(Constants.SKILLS_SUMMONING);
                            }
                        },
                        new Obstacle(methods, "Huge Gate", 3944, "Enter",
                                new Tile(2461, 3384, 0), new Tile(2461, 3380, 0), new Tile(2461, 3375, 0),
                                new Tile(2457, 3371, 0), new Tile(2452, 3369, 0), new Tile(2447, 3368, 0),
                                new Tile(2442, 3368, 0), new Tile(2437, 3367, 0), new Tile(2432, 3366, 0),
                                new Tile(2427, 3366, 0), new Tile(2422, 3364, 0), new Tile(2418, 3362, 0),
                                new Tile(2413, 3361, 0), new Tile(2408, 3359, 0), new Tile(2403, 3356, 0),
                                new Tile(2399, 3354, 0), new Tile(2394, 3351, 0), new Tile(2390, 3347, 0),
                                new Tile(2387, 3343, 0), new Tile(2386, 3338, 0), new Tile(2385, 3333, 0)) {
                            @Override
                            public boolean accept() {
                                final int y = methods.context.players.local().tile().y();
                                return y >= 3335 && y <= 3385;
                            }
                        },
                        new Obstacle(methods, "Log Balance", 3933, "Cross",
                                new Tile(2386, 3333, 0), new Tile(2386, 3329, 0), new Tile(2384, 3324, 0),
                                new Tile(2380, 3321, 0), new Tile(2375, 3319, 0), new Tile(2370, 3318, 0),
                                new Tile(2365, 3319, 0), new Tile(2360, 3320, 0), new Tile(2357, 3315, 0),
                                new Tile(2357, 3310, 0), new Tile(2357, 3305, 0), new Tile(2352, 3303, 0),
                                new Tile(2347, 3302, 0), new Tile(2342, 3300, 0), new Tile(2337, 3299, 0),
                                new Tile(2333, 3295, 0), new Tile(2331, 3290, 0), new Tile(2335, 3287, 0),
                                new Tile(2340, 3289, 0), new Tile(2343, 3293, 0), new Tile(2348, 3295, 0),
                                new Tile(2353, 3295, 0), new Tile(2356, 3291, 0), new Tile(2357, 3286, 0),
                                new Tile(2357, 3281, 0), new Tile(2354, 3278, 0), new Tile(2349, 3277, 0),
                                new Tile(2344, 3278, 0), new Tile(2339, 3280, 0), new Tile(2334, 3281, 0),
                                new Tile(2333, 3276, 0), new Tile(2335, 3272, 0), new Tile(2340, 3269, 0),
                                new Tile(2345, 3269, 0), new Tile(2349, 3265, 0), new Tile(2348, 3260, 0),
                                new Tile(2344, 3258, 0), new Tile(2341, 3254, 0), new Tile(2345, 3251, 0),
                                new Tile(2346, 3246, 0), new Tile(2347, 3241, 0), new Tile(2342, 3239, 0),
                                new Tile(2337, 3239, 0), new Tile(2334, 3243, 0), new Tile(2333, 3248, 0),
                                new Tile(2330, 3252, 0), new Tile(2325, 3253, 0), new Tile(2320, 3253, 0),
                                new Tile(2315, 3253, 0), new Tile(2310, 3252, 0), new Tile(2305, 3251, 0),
                                new Tile(2300, 3250, 0), new Tile(2295, 3248, 0), new Tile(2293, 3243, 0),
                                new Tile(2291, 3238, 0)) {
                            @Override
                            public boolean accept() {
                                final int y = methods.context.players.local().tile().y(),
                                        x = methods.context.players.local().tile().x();
                                return y >= 3237 && y <= 3335 && x >= 2286 && x <= 2418;
                            }
                        },
                        new Obstacle(methods, "Dense Forest 1", 3938, "Enter",
                                new Tile(2290, 3233, 0), new Tile(2294, 3230, 0), new Tile(2298, 3227, 0),
                                new Tile(2302, 3224, 0)) {
                            @Override
                            public boolean accept() {
                                final int y = methods.context.players.local().tile().y(),
                                        x = methods.context.players.local().tile().x();
                                return y >= 3224 && y <= 3233 && x >= 2286 && x <= 2307;
                            }
                        },
                        new Obstacle(methods, "Dense Forest 2", 3939, "Enter") {
                            @Override
                            public boolean accept() {
                                final int y = methods.context.players.local().tile().y(),
                                        x = methods.context.players.local().tile().x();
                                return y == 3222 && x >= 2303 && x <= 2304;
                            }
                        },
                        new Obstacle(methods, "Dense Forest 3", 3938, "Enter") {
                            @Override
                            public boolean accept() {
                                final int y = methods.context.players.local().tile().y(),
                                        x = methods.context.players.local().tile().x();
                                return y == 3219 && x >= 2303 && x <= 2304;
                            }
                        },
                        new Obstacle(methods, "Dense Forest 4", 3937, "Enter") {
                            @Override
                            public boolean accept() {
                                final int y = methods.context.players.local().tile().y(),
                                        x = methods.context.players.local().tile().x();
                                return y == 3216 && x >= 2302 && x <= 2304;
                            }
                        },
                        new Obstacle(methods, "Leaves", 3925, "Jump",
                                new Tile(2303, 3213, 0), new Tile(2301, 3209, 0), new Tile(2296, 3207, 0),
                                new Tile(2291, 3208, 0), new Tile(2286, 3210, 0), new Tile(2281, 3212, 0),
                                new Tile(2276, 3212, 0), new Tile(2271, 3210, 0), new Tile(2267, 3205, 0)) {
                            @Override
                            public boolean accept() {
                                final int y = methods.context.players.local().tile().y(),
                                        x = methods.context.players.local().tile().x();
                                return y >= 3204 && y <= 3214 && x >= 2264 && x <= 2309;
                            }
                        },
                        new Obstacle(methods, "Log Balance 2", 3932, "Cross",
                                new Tile(2267, 3201, 0), new Tile(2266, 3199, 0), new Tile(2263, 3195, 0),
                                new Tile(2261, 3190, 0), new Tile(2261, 3185, 0), new Tile(2257, 3182, 0),
                                new Tile(2252, 3182, 0), new Tile(2247, 3184, 0), new Tile(2243, 3187, 0),
                                new Tile(2241, 3192, 0), new Tile(2241, 3197, 0), new Tile(2240, 3202, 0),
                                new Tile(2239, 3207, 0), new Tile(2238, 3212, 0), new Tile(2238, 3217, 0),
                                new Tile(2238, 3222, 0), new Tile(2238, 3227, 0), new Tile(2240, 3232, 0),
                                new Tile(2240, 3237, 0), new Tile(2241, 3242, 0), new Tile(2241, 3247, 0),
                                new Tile(2243, 3252, 0), new Tile(2248, 3253, 0), new Tile(2253, 3254, 0),
                                new Tile(2258, 3250, 0)) {
                            @Override
                            public boolean accept() {
                                final int y = methods.context.players.local().tile().y(),
                                        x = methods.context.players.local().tile().x();
                                return y >= 3169 && y <= 3257 && x >= 2222 && x <= 2273;
                            }
                        },
                        new Obstacle(methods, "Renew Points", Invariants.SMALL_OBELISK, "Renew points") {
                            @Override
                            public boolean accept() {
                                return methods.context.players.local().tile().y() >= 3385
                                        && methods.context.skills.realLevel(Constants.SKILLS_SUMMONING)
                                        < methods.context.skills.realLevel(Constants.SKILLS_SUMMONING);
                            }
                        }
                };
            }
        },

        TIRANNWN_LODESTONE(new Area(new Tile(3540, 3598, 0), new Tile(3366, 3797, 0))) {
            @Override
            public TilePath getPathToBank(final Methods methods) {
                return new TilePath(methods.context, new Tile[]{new Tile(3450, 3704, 0),
                        new Tile(3450, 3709, 0),
                        new Tile(3449, 3714, 0),
                        new Tile(3448, 3719, 0)});
            }

            @Override
            public void teleport(final Methods methods) {
                if (methods.actionBar.isExpanded()) {
                    if (methods.actionBar.isLocked()) {
                        final Component node = methods.actionBar.getNode(10);
                        if (node != null && node.interact("Teleport to Daemonheim")) {
                            methods.sleep.sleep(new Condition() {
                                @Override
                                public boolean accept() {
                                    return !methods.context.players.local().idle();
                                }
                            }, 3000);
                        }
                    } else {
                        if (methods.actionBar.lock(true)) {
                            methods.sleep.sleep(new Condition() {
                                @Override
                                public boolean accept() {
                                    return !methods.actionBar.isLocked();
                                }
                            }, 2000);
                        }
                    }
                } else {
                    if (methods.actionBar.expand(true)) {
                        methods.sleep.sleep(new Condition() {
                            @Override
                            public boolean accept() {
                                return !methods.actionBar.isExpanded();
                            }
                        }, 2000);
                    }
                }
            }

            @Override
            public Obstacle[] getReturnPath(Methods methods) {
                return new Obstacle[]{
                        new Obstacle(methods, "Log Balance", 3932, "Cross",
                                new Tile(2239, 3184, 0), new Tile(2241, 3188, 0), new Tile(2242, 3193, 0),
                                new Tile(2243, 3198, 0), new Tile(2243, 3203, 0), new Tile(2242, 3208, 0),
                                new Tile(2239, 3213, 0), new Tile(2238, 3218, 0), new Tile(2238, 3223, 0),
                                new Tile(2238, 3228, 0), new Tile(2240, 3233, 0), new Tile(2241, 3238, 0),
                                new Tile(2241, 3243, 0), new Tile(2241, 3248, 0), new Tile(2244, 3251, 0),
                                new Tile(2249, 3253, 0), new Tile(2254, 3254, 0), new Tile(2259, 3254, 0)) {
                            @Override
                            public boolean accept() {
                                final int y = methods.context.players.local().tile().y(),
                                        x = methods.context.players.local().tile().x();
                                return y >= 3181 && y <= 3257 && x >= 2237 && x <= 2260;
                            }
                        },
                        new Obstacle(methods, "Sticks", 3922, "Pass",
                                new Tile(2254, 3144, 0), new Tile(2249, 3146, 0), new Tile(2245, 3149, 0),
                                new Tile(2242, 3153, 0), new Tile(2240, 3157, 0), new Tile(2239, 3162, 0),
                                new Tile(2236, 3167, 0), new Tile(2234, 3171, 0), new Tile(2230, 3174, 0),
                                new Tile(2234, 3181, 0)) {
                            @Override
                            public boolean accept() {
                                if (methods.context.players.local().tile().equals(new Tile(2238, 3181, 0))) {
                                    return false;
                                }
                                final int py = methods.context.players.local().tile().y();

                                return py <= 3184;
                            }
                        }
                };
            }
        };

        private final Area bankArea;

        Method(final Area bankArea) {
            this.bankArea = bankArea;
        }

        public abstract TilePath getPathToBank(final Methods methods);

        public abstract void teleport(final Methods methods);

        public abstract Obstacle[] getReturnPath(final Methods methods);
    }
}