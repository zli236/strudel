package com.nec.strudel.bench.micro.interactions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.nec.strudel.bench.micro.entity.Post;
import com.nec.strudel.bench.micro.interactions.base.AbstractListMyPosts.OutParam;
import com.nec.strudel.bench.micro.interactions.entity.ListMyPosts;
import com.nec.strudel.bench.micro.params.SessionParam;
import com.nec.strudel.bench.micro.params.TransitionParam;
import com.nec.strudel.bench.test.EntityAssert;
import com.nec.strudel.bench.test.interactions.AbstractInteractionTestBase;
import com.nec.strudel.bench.test.interactions.TestOn;
import com.nec.strudel.session.Param;
import com.nec.strudel.session.Result;
import com.nec.strudel.session.impl.State;

@TestOn(ListMyPosts.class)
public class ListMyPostsTest extends AbstractInteractionTestBase {
	@Test
	public void testPrepare() {
		final int userId = 2;
		final int minUserId = 1;
		final int userNum = 3;

		State state = newState()
		.put(SessionParam.USER_ID, userId)
		.put(SessionParam.MIN_USER_ID, minUserId)
		.put(SessionParam.USER_NUM, userNum);
		Param param = prepare(state);
		assertEquals(userId, param.getInt(SessionParam.USER_ID));
	}

	@Test
	public void testExecute() {
		final int userId = 2;
		final int size = 4;
		List<Post> data = populateList(Post.class,
				size, new ContentBuilder<Post>("userId", userId));

		Result res = executor()
				.param(SessionParam.USER_ID, userId)
				.executeSuccess();
		List<Post> posts = res.get(OutParam.POST_LIST);
		EntityAssert.assertSameEntitySets(data, posts);

		Post p = (Post) complete(res).get(TransitionParam.POST);
		assertNotNull(p);
		EntityAssert.assertContains(p, data);
	}

}
