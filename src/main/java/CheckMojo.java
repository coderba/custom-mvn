import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Echos an object string to the output screen.
 *
 * @goal check
 * @requiresProject false
 */
public class CheckMojo extends AbstractMojo {
    /**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @readonly
     */
    private MavenProject project;

    //gets assetup file paths from pom.xml
    /**
     * @parameter
     */
    private ArrayList includeFiles = new ArrayList();
    private boolean ableToLog = true;

    public void execute() throws MojoExecutionException, MojoFailureException {

        getLog().info(Constants.CRONJOBS_CHECK_BEGIN);
        getLog().info(includeFiles.get(0).toString());
        //reads file and check cronjob lines.
        if(includeFiles.size()>0){
            //can not write foreach loop because of compilation error on some environments for MOJO.
            for(int i=0; i<includeFiles.size(); i++){
                readFile(includeFiles.get(i).toString(), "2>&1");
            }
        }

        verification(); //throws mojoFailureException
    }

    private void readFile(String fileName, String suffix) {

        BufferedReader bufferedReader = null;
        FileReader fileReader = null;

        try {
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            String currentLineSTR;

            while ((currentLineSTR = bufferedReader.readLine()) != null) {
                checkCronjobLines(currentLineSTR, suffix);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();

                if (fileReader != null)
                    fileReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void checkCronjobLines(String currentLineSTR, String suffix){
        if (currentLineSTR.contains("install_cronjob") && !currentLineSTR.contains("uninstall_cronjob")) {
            if (currentLineSTR.contains(suffix)) {
                ableToLog &= true;
                getLog().info(currentLineSTR +" >>> "+ Constants.CONFIRMED);
            } else {
                ableToLog &= false;
                getLog().info(currentLineSTR + " >>> "+Constants.NOT_CONFIRMED);
            }
        }
    }

    private void verification() throws MojoFailureException{
        if (ableToLog) {
            getLog().info(Constants.VERIFICATION_SUCCESSFULL);
        } else {
            getLog().debug(Constants.VERIFICATION_FAIL);
            throw new MojoFailureException(Constants.VERIFICATION_FAIL_REASON);
        }
    }

}