package uk.co.jrtapsell.fyp.gitWrapper

import org.testng.Assert
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import uk.co.jrtapsell.fyp.gitWrapper.data.SignatureStatus
import java.lang.management.ManagementFactory

/**
 * @author James Tapsell
 */
class TestGit {

    /** Tries to load each of the test repositories. */
    @Test(dataProvider = "repos", dataProviderClass = RepoProvider::class)
    fun `Constructs a repo for a known path `(path: String) {
        Git(path)
    }

    /** Lists all of the commits in each of the test repos. */
    @Test(dataProvider = "repos", dataProviderClass = RepoProvider::class)
    fun `Lists commits for the given repo `(path: String) {
        val repository = Git(path)
        val commits = repository.listCommits()
        Assert.assertNotEquals(commits.size, 0)
        for (commit in commits) {
            println(commit)
        }
    }

    /** Checks that the genesis hash of this repo is correct. */
    @Test
    fun `Validates known facts about this repo`() {
        val repository = Git("../gitRepos/android_app_verifier")
        val commits = repository.listCommits()
        val genesis = commits.last()
        Assert.assertEquals(genesis.commitHash.value(), "0b764cd867bff6e471cca0ab009d4874c2b85819")
        Assert.assertEquals(genesis.commitHash.last(6), "b85819")
    }

    /** Tries to load a file as a repo. */
    @Test(
            expectedExceptions = arrayOf(GitException::class),
            expectedExceptionsMessageRegExp = "Couldn't list commits, /dev/null is not a directory"
    )
    fun `Tries for a file that is not a repo`() {
        val repo = Git("/dev/null")
        repo.listCommits()
    }

    /** Tries to load a directory as a repo. */
    @Test(
            expectedExceptions = arrayOf(GitException::class),
            expectedExceptionsMessageRegExp = "Couldn't list commits, /tmp is not a git repo"
    )
    fun `Tries for a directory that is not a repo`() {
        val repo = Git("/tmp")
        repo.listCommits()
    }

    /** Checks the status of the current repo. */
    @Test
    fun `Checks the local repo is securely signed`() {
        val repo = Git("./")
        Assert.assertEquals(repo.getState(), SignatureStatus.GOOD)
    }

    /** Checks each of the test repos have the expected status values. */
    @Test(dataProviderClass = RepoProvider::class, dataProvider = "repos")
    fun `Checks the status of known repos `(repo: Repo) {
        Assert.assertEquals(Git(repo.path).getState(), repo.state)
    }
}