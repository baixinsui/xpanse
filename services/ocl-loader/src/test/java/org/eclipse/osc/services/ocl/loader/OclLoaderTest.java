package org.eclipse.osc.services.ocl.loader;

import org.apache.karaf.minho.boot.Minho;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Optional;
import java.util.stream.Stream;

public class OclLoaderTest {

    @Test
    public void loading() throws Exception {
        Minho minho = Minho.builder().loader(() -> Stream.of(new OclLoader())).build().start();

        OclLoader oclLoader = minho.getServiceRegistry().get(OclLoader.class);

        Ocl ocl = oclLoader.getOcl(new File("target/test-classes/test.json").toURI().toURL());

        Assertions.assertNotNull(ocl);

        Assertions.assertEquals("flat", ocl.getBilling().getModel());
        Assertions.assertEquals("euro", ocl.getBilling().getCurrency());
        Assertions.assertEquals("monthly", ocl.getBilling().getPeriod());
        Assertions.assertEquals("instance", ocl.getBilling().getVariableItem());
        Assertions.assertEquals(20.0, ocl.getBilling().getFixedPrice());
        Assertions.assertEquals(10.0, ocl.getBilling().getVariablePrice());

        Assertions.assertEquals("ubuntu-x64", ocl.getImage().getBase().get(0).getName());
    }

    @Test
    public void testBlockAssociationProvisioner() throws Exception {
        Minho minho = Minho.builder().loader(() -> Stream.of(new OclLoader())).build().start();

        OclLoader oclLoader = minho.getServiceRegistry().get(OclLoader.class);

        Ocl ocl = oclLoader.getOcl(new File("target/test-classes/test.json").toURI().toURL());

        Assertions.assertNotNull(ocl);

        String kafka_provisioner_s = ocl.getImage().getArtifacts().get(0).getProvisioners().get(0);
        Optional<Provisioner> kafka_provisioner = ocl.referTo(kafka_provisioner_s, Provisioner.class);
        Assertions.assertEquals("my-kafka-release", kafka_provisioner.get().getName());
        Assertions.assertEquals("shell", kafka_provisioner.get().getType());
        Assertions.assertEquals("WORK_HOME=/usr1/KAFKA/", kafka_provisioner.get().getEnvironment_vars().get(0));
        Assertions.assertEquals("echo $PATH", kafka_provisioner.get().getInline().get(1));
    }

    @Test
    public void testBlockAssociationArtifact() throws Exception {
        Minho minho = Minho.builder().loader(() -> Stream.of(new OclLoader())).build().start();

        OclLoader oclLoader = minho.getServiceRegistry().get(OclLoader.class);

        Ocl ocl = oclLoader.getOcl(new File("target/test-classes/test.json").toURI().toURL());

        Assertions.assertNotNull(ocl);

        VM vm = ocl.getCompute().getVm().get(0);
        Assertions.assertEquals("my-vm", vm.getName());
        Assertions.assertEquals("$.image.artifacts[0]", vm.getImage());

        Optional<Artifact> artifact = ocl.referTo(vm.getImage(), Artifact.class);
        Optional<BaseImage> base_image = ocl.referTo(artifact.get().getBase(), BaseImage.class);
        Assertions.assertEquals("ubuntu-x64", base_image.get().getName());
    }

}
