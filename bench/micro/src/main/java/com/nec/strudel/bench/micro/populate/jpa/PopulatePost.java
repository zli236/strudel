/*******************************************************************************
 * Copyright 2015, 2016 Junichi Tatemura
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package com.nec.strudel.bench.micro.populate.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.nec.strudel.bench.micro.entity.Post;
import com.nec.strudel.bench.micro.populate.ContentSet;
import com.nec.strudel.bench.micro.populate.base.AbstractPopulatePost;
import com.nec.strudel.workload.api.Populator;

public class PopulatePost extends AbstractPopulatePost<EntityManager>
        implements Populator<EntityManager, ContentSet> {
    static final String QUERY = "SELECT e FROM Post e WHERE e.userId = :uid";
    static final String PARAM_UID = "uid";

    @Override
    public void process(EntityManager db, ContentSet param) {
        int userId = param.getGroupId();
        String[] contents = param.getContents();
        db.getTransaction().begin();
        for (int i = 0; i < contents.length; i++) {
            Post item = new Post(userId);
            item.setContent(contents[i]);
            db.persist(item);
        }
        db.getTransaction().commit();
    }

    @Override
    protected List<Post> getPostsByUser(EntityManager em, int userId) {
        TypedQuery<Post> query = em.createQuery(QUERY, Post.class);
        query.setParameter(PARAM_UID, userId);
        return query.getResultList();
    }

}
