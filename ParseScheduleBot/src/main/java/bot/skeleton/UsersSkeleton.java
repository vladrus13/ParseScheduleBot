package bot.skeleton;

import java.util.HashMap;
import java.util.Map;

public class UsersSkeleton {

    private static final MainSkeleton mainSkeleton = new MainSkeleton();

    private static final Map<String, Skeleton> skeletons = new HashMap<>();

    public static Skeleton get(String chatId) {
        return skeletons.getOrDefault(chatId, mainSkeleton);
    }

    public static void set(String chatId, Skeleton skeleton) {
        skeletons.put(chatId, skeleton);
    }
}
