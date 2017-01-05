package com.rizomm.ipii.steven.dao;


import com.rizomm.ipii.steven.model.Category;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rizomm.ipii.steven.helper.Utils.*;
import static com.rizomm.ipii.steven.model.Category.DELETE_ALL;
import static com.rizomm.ipii.steven.model.Category.FIND_ALL;

/**
 * Created by steven on 17/11/2016.
 */

@Stateless
@Named
public class CategoryDao implements ICategoryDao, Serializable {

    @PersistenceContext(unitName = "projectJ2ee")
    protected EntityManager em;
    protected boolean isNotTest = true;

    @Override
    public Category createCategory(final Category category) {
        if (isNotEmpty(category.getLabel())) {
            em.persist(category);
            if (isNotTest) {
                em.flush();
            }
            return category;
        }
        return null;
    }

    @Override
    public Category findCategoryById(final int idCategory) {
        final Category findCategory = em.find(Category.class, idCategory);
        return findCategory;
    }

    @Override
    public List<Category> findAllCategory() {
        final TypedQuery<Category> query = em.createNamedQuery(FIND_ALL, Category.class);
        if (isNotTest) {
            em.joinTransaction();
        }
        return query.getResultList();
    }

    @Override
    public void deleteAllCategory() {
        em.createNamedQuery(DELETE_ALL, Category.class).executeUpdate();
    }

    @Override
    public Boolean deleteCategoryById(final int idCategory) {
        final Category category = em.find(Category.class, idCategory);
        if (isNotEmpty(category)) {
            return deleteCategory(category);
        }
        return false;
    }

    @Override
    public Boolean deleteCategory(final Category category) {
        em.remove(category);
        final Category findCategory = em.find(Category.class, category.getId());
        if (isEmpty(findCategory)) {
            return true;
        }
        return false;
    }

    @Override
    public Category updateCategory(final Category category) {
        return em.merge(category);
    }

    @Override
    public Map<String, Object> convertJsonToProduct(final String categoryString) {
        JSONObject jsonCategory = null;
        final Map<String, Object> result = new HashMap();
        final Category category = new Category();

        try {

            jsonCategory = new JSONObject(categoryString);

            if (isNotEmpty(jsonCategory, "id")) {

                final String idString = jsonCategory.getString("id");

                if (!isInt(idString)) {
                    return generateMessageError400("L'id de la Category doit être un chiffre ! ");
                }

                category.setId(Integer.parseInt(idString));

            } else if (isNotEmpty(jsonCategory, "label")) {

                if (isTooLarge(jsonCategory, "label", 255)) {
                    return generateMessageError400("Le label de la catégorie est trop long !");
                }

                category.setLabel(jsonCategory.getString("label"));
            } else {
                return generateMessageError400("La category est mal paramétrée ! ");
            }

        } catch (JSONException e) {
            return generateMessageError400(e.getMessage());
        }

        result.put("ERROR", false);
        result.put("CATEGORY", category);
        return result;

    }

}
