package avsoftware.com.skydemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import avsoftware.com.skydemo.cache.MovieCache;
import avsoftware.com.skydemo.ui.MovieActivityViewModel;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class MovieActivityViewModelTest {

    @Mock
    private MovieCache cache;

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void example_test_ViewModel(){
        MovieActivityViewModel viewModel = new MovieActivityViewModel(cache);

        // TODO: Test Things
        assertNotNull(viewModel);

        assertNotNull(cache);
    }
}