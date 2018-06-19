/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xaris.xoulis.letsbake.utils;


import com.xaris.xoulis.letsbake.data.model.Ingredient;
import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.data.model.Step;

import java.util.List;

public class TestUtil {
    public static Recipe createRecipe(int id, String name, List<Ingredient> ingredients, List<Step> steps, int servings, String image, int imageSrcId) {
        return new Recipe(id, name, ingredients, steps, servings, image, imageSrcId);
    }
}
