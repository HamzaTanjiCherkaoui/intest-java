package intest.learningSpike;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Consumer;

public class ConsumerTests {

    public static void main(String[] args) {
        runConsumer(new MyConsumer());

        runConsumer(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        });
        runConsumer((String s) -> System.out.println(s));
        runConsumer(s -> System.out.println(s));
        runConsumer(System.out::println); // method reference

        Consumer<String> myFileWriterConsumer = new MyFileWriter(Paths.get("C:", "temp", "intestfile.txt"));
        runConsumer(myFileWriterConsumer);

        runConsumer(createFileConsumer(Paths.get("C:", "temp", "intestfile2.txt")));
    }

    // Consumer : F S => () (void)
    static void runConsumer(Consumer<String> c) {
        String myTestString = "Hello World";
        c.accept(myTestString);
    }

    static Consumer<String> createFileConsumer(final Path path) {
        return s -> {
            try {
                Files.write(path, Arrays.asList(s), Charset.forName("UTF-8"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static final class MyConsumer implements Consumer<String> {
        @Override
        public void accept(String s) {
            System.out.println(s);
        }
    }

    // écrire des données dnas un fichier
    private static final class MyFileWriter implements Consumer<String> {
        private final Path file; // file vers lequel on écrit

        public MyFileWriter(Path file) {
            this.file = file;
        }

        @Override
        public void accept(String s) {
            try {
                Files.write(file, Arrays.asList(s), Charset.forName("UTF-8"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
