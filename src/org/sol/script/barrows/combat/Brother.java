package org.sol.script.barrows.combat;

public class Brother {
    public enum Brothers {
        AHRIM(-1, "Ahrim the Blighted", AttackStyle.MAGIC),
        DHAROK(-1, "Dharok the Wretched", AttackStyle.MELEE),
        GUTHAN(-1, "Guthan the Infested", AttackStyle.MELEE),
        TORAG(-1, "Torag the Corrupted", AttackStyle.MELEE),
        VERAC(-1, "Verac the Defiled", AttackStyle.MELEE),
        AKRISAE(-1, "Akrisae the Doomed", AttackStyle.MAGIC),
        KARIL(-1, "Karil the Tainted", AttackStyle.MELEE);

        private final String name;
        private final int id;
        private final AttackStyle attackStyle;

        Brothers(final int id, final String name, final AttackStyle attackStyle) {
            this.id = id;
            this.name = name;
            this.attackStyle = attackStyle;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public AttackStyle getAttackStyle() {
            return attackStyle;
        }
    }

    public enum AttackStyle {
        MELEE(),
        MAGIC();
    }
}
