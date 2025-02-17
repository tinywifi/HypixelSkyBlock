package net.swofty.types.generic.event.actions.custom.skill;

import net.minestom.server.event.Event;
import net.swofty.types.generic.data.datapoints.DatapointSkills;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.CustomBlockBreakEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.SkillableMine;
import net.swofty.types.generic.skill.SkillCategories;

@EventParameters(description = "Handles mining-related progression for Skills",
        node = EventNodes.CUSTOM,
        requireDataLoaded = true)
public class ActionSkillMiningHandler extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return CustomBlockBreakEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        CustomBlockBreakEvent event = (CustomBlockBreakEvent) tempEvent;
        SkyBlockItem item = new SkyBlockItem(event.getMaterial());

        if (item.getGenericInstance() == null) return;
        Object instance = item.getGenericInstance();

        if (instance instanceof SkillableMine skillableMine) {
            SkillCategories skillCategory = skillableMine.getSkillCategory();
            DatapointSkills.PlayerSkills skills = event.getPlayer().getSkills();

            skills.set(event.getPlayer(), skillCategory, skills.get(skillCategory) + skillableMine.getMiningValueForSkill());
        }
    }
}
